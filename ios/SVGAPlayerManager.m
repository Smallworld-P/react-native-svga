//
//  SVGAPlayerManager.m
//  Svga
//
//  Created by koren on 2021/3/5.
//  Copyright © 2021 Facebook. All rights reserved.
//

#import "SVGAPlayerManager.h"
#import "SVGAPlayer.h"
#import "SVGAParser.h"
#import <objc/runtime.h>
#import <React/RCTBridge.h>

@interface SVGAPlayer (React)<SVGAPlayerDelegate>

@property(nonatomic, copy) NSString *source;
@property(nonatomic, copy) NSString *currentState;
@property(nonatomic, assign) NSInteger toFrame;
@property(nonatomic, assign) NSInteger toPercentage;
@property(nonatomic, copy) RCTBubblingEventBlock onFinished;
@property(nonatomic, copy) RCTBubblingEventBlock onFrame;
@property(nonatomic, copy) RCTBubblingEventBlock onPercentage;

@end

@implementation SVGAPlayer (React)

static int kReactSourceIdentifier;
static int kReactCurrentStateIdentifier;
static int kReactOnFinishedIdentifier;
static int kReactOnFrameIdentifier;
static int kReactOnPercentageIdentifier;

- (void)loadWithSource:(NSString *)source {
    SVGAParser *parser = [[SVGAParser alloc] init];
    if ([source hasPrefix:@"http"] || [source hasPrefix:@"https"]) {
        [parser parseWithURL:[NSURL URLWithString:source]
             completionBlock:^(SVGAVideoEntity *_Nullable videoItem) {
               [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                 [self setVideoItem:videoItem];
                 [self startAnimation];
               }];
             }
                failureBlock:nil];
    } else {
        NSString *localPath = [[NSBundle mainBundle] pathForResource:source ofType:@"svga"];
        if (localPath != nil) {
            [parser parseWithData:[NSData dataWithContentsOfFile:localPath]
                         cacheKey:source
                  completionBlock:^(SVGAVideoEntity *_Nonnull videoItem) {
                    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                      [self setVideoItem:videoItem];
                      [self startAnimation];
                    }];
                  }
                     failureBlock:nil];
        }
    }
}

- (void)setSource:(NSString *)source {
    if ([source isKindOfClass:[NSString class]] && ([self source] == nil || ![source isEqualToString:[self source]])) {
        objc_setAssociatedObject(self, &kReactSourceIdentifier, source, OBJC_ASSOCIATION_COPY_NONATOMIC);
        [self loadWithSource:source];
    }
}

- (NSString *)source {
    return objc_getAssociatedObject(self, &kReactSourceIdentifier);
}

- (void)setCurrentState:(NSString *)currentState {
    if ([currentState isKindOfClass:[NSString class]] &&
        ([self currentState] == nil || ![currentState isEqualToString:[self currentState]])) {
        objc_setAssociatedObject(self, &kReactCurrentStateIdentifier, currentState, OBJC_ASSOCIATION_COPY_NONATOMIC);
        if ([currentState isEqualToString:@"start"]) {
            [self startAnimation];
        } else if ([currentState isEqualToString:@"pause"]) {
            [self pauseAnimation];
        } else if ([currentState isEqualToString:@"stop"]) {
            [self stopAnimation];
        } else if ([currentState isEqualToString:@"clear"]) {
            [self stopAnimation];
            [self clear];
        }
    }
}

- (NSString *)currentState {
    return objc_getAssociatedObject(self, &kReactCurrentStateIdentifier);
}

- (void)setOnFinished:(RCTBubblingEventBlock)onFinished {
    objc_setAssociatedObject(self, &kReactOnFinishedIdentifier, onFinished, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (RCTBubblingEventBlock)onFinished {
    return objc_getAssociatedObject(self, &kReactOnFinishedIdentifier);
}

- (void)setOnFrame:(RCTBubblingEventBlock)onFrame {
    objc_setAssociatedObject(self, &kReactOnFrameIdentifier, onFrame, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (RCTBubblingEventBlock)onFrame {
    return objc_getAssociatedObject(self, &kReactOnFrameIdentifier);
}

- (void)setOnPercentage:(RCTBubblingEventBlock)onPercentage {
    objc_setAssociatedObject(self, &kReactOnPercentageIdentifier, onPercentage, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (RCTBubblingEventBlock)onPercentage {
    return objc_getAssociatedObject(self, &kReactOnPercentageIdentifier);
}

/// 设置动画到某帧
/// @param toFrame 帧值
- (void)setToFrame:(NSInteger)toFrame {
    if (toFrame < 0) {
        return;
    }
    [self stepToFrame:toFrame andPlay:[self.currentState isEqualToString:@"play"]];
}

/// 设置动画帧的初始值
- (NSInteger)toFrame {
    return 0;
}

/// 设置动画进度
/// @param toPercentage 进度值
- (void)setToPercentage:(NSInteger)toPercentage {
    if (toPercentage < 0) {
        return;
    }
    [self stepToPercentage:toPercentage andPlay:[self.currentState isEqualToString:@"play"]];
}

/// 设置动画进度初始值
- (NSInteger)toPercentage {
    return 0.0;
}



#pragma mark - SVGAPlayerDelegate

/// 动画播放完成
/// @param player player
- (void)svgaPlayerDidFinishedAnimation:(SVGAPlayer *)player {
    if (self.onFinished) {
        self.onFinished(@{});
    }
}

/// 控制当前动画停靠在某帧，如果 currentState 值为 ‘play’，则跳到该帧后继续播放动画
/// @param frame 某帧
- (void)svgaPlayerDidAnimatedToFrame:(NSInteger)frame {
    if (self.onFrame) {
        self.onFrame(@{ @"value" : @(frame) });
    }
}

/// 控制当前动画停靠在某进度，如果 currentState 值为 ‘play’，则跳到该帧后继续播放动画
/// @param percentage 某进度
- (void)svgaPlayerDidAnimatedToPercentage:(CGFloat)percentage {
    if (self.onPercentage) {
        self.onPercentage(@{ @"value" : @(percentage) });
    }
}

@end

@interface SVGAPlayerManager ()

@end

@implementation SVGAPlayerManager

static NSOperationQueue *cacheQueue;

RCT_EXPORT_MODULE(RNSVGAManager)
/// 默认值为 0，用于指定动画循环次数，0 = 无限循环
RCT_EXPORT_VIEW_PROPERTY(loops, NSInteger)
/// 默认值为 true，动画播放完成后，是否清空画布
RCT_EXPORT_VIEW_PROPERTY(clearsAfterStop, BOOL)
/// SVGA 动画文件的路径，可以是 URL，或是本地 NSBundle.mainBundle / assets 文件
RCT_EXPORT_VIEW_PROPERTY(source, NSString)
/// 用于控制 SVGA 播放状态，可设定以下值
/// ‘start’ = 从头开始播放;
/// ‘pause’ = 从当前位置暂停播放;
/// ‘stop’ = ‘停止播放’;
/// ‘clear’ = ‘停止播放并清空画布
RCT_EXPORT_VIEW_PROPERTY(currentState, NSString)
/// 控制当前动画停靠在某帧，如果 currentState 值为 ‘play’，则跳到该帧后继续播放动画
RCT_EXPORT_VIEW_PROPERTY(toFrame, NSInteger)
/// 控制当前动画停靠在某进度，如果 currentState 值为 ‘play’，则跳到该帧后继续播放动画
RCT_EXPORT_VIEW_PROPERTY(toPercentage, NSInteger)
/// 动画播放完成后，回调
RCT_EXPORT_VIEW_PROPERTY(onFinished, RCTBubblingEventBlock)
/// 动画播放至某帧时，回调
RCT_EXPORT_VIEW_PROPERTY(onFrame, RCTBubblingEventBlock)
/// 动画播放至某进度时，回调
RCT_EXPORT_VIEW_PROPERTY(onPercentage, RCTBubblingEventBlock)
/// 预加载缓存
/// @param cacheUrls 缓存的数据
RCT_EXPORT_METHOD(advanceDownload:(NSArray *)cacheUrls) {
    if (cacheUrls.count <= 0) {
        return;
    }
    cacheQueue = [NSOperationQueue new];
    cacheQueue.maxConcurrentOperationCount = 1;
    [cacheQueue addOperationWithBlock:^{
        for (NSString *source in cacheUrls) {
            SVGAParser *parser = [[SVGAParser alloc] init];
            if ([source hasPrefix:@"http"] || [source hasPrefix:@"https"]) {
                [parser parseWithURL:[NSURL URLWithString:source]
                     completionBlock:^(SVGAVideoEntity *_Nullable videoItem) {
                        NSLog(@"预加载完成");
                     }
                        failureBlock:nil];
            } else {
                NSString *localPath = [[NSBundle mainBundle] pathForResource:source ofType:@"svga"];
                if (localPath != nil) {
                    [parser parseWithData:[NSData dataWithContentsOfFile:localPath]
                                 cacheKey:source
                          completionBlock:^(SVGAVideoEntity *_Nonnull videoItem) {
                            NSLog(@"预加载完成");
                          }
                             failureBlock:nil];
                }
            }
        }
    }];
}

/// 初始化视图
- (UIView *)view {
    SVGAPlayer *aPlayer = [[SVGAPlayer alloc] init];
    aPlayer.delegate = aPlayer;
    return aPlayer;
}

@end

