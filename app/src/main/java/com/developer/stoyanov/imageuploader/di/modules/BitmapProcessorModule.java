package com.developer.stoyanov.imageuploader.di.modules;

import com.developer.stoyanov.imageuploader.processor.Base64Coder;
import com.developer.stoyanov.imageuploader.processor.BitmapCompressor;
import com.developer.stoyanov.imageuploader.processor.BitmapOptimizer;

import dagger.Module;
import dagger.Provides;

@Module
public class BitmapProcessorModule {

    @Provides
    Base64Coder provideCoderBase64() {
        return new Base64Coder();
    }

    @Provides
    BitmapOptimizer provideOptimizationBitmap() {
        return new BitmapOptimizer();
    }

    @Provides
    BitmapCompressor provideBitmapCompressor() {
        return new BitmapCompressor();
    }
}
