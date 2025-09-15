package com.lazyarts.vikram.cached_video_player;

import android.content.Context;
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.view.TextureRegistry;

/** CachedVideoPlayerPlugin */
public class CachedVideoPlayerPlugin implements FlutterPlugin, VideoPlayerApi {

  private FlutterState flutterState;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    flutterState =
        new FlutterState(
            flutterPluginBinding.getApplicationContext(),
            flutterPluginBinding.getBinaryMessenger(),
            flutterPluginBinding.getFlutterAssets()::getAssetFilePathByName,
            flutterPluginBinding.getFlutterAssets()::getAssetFilePathByName,
            flutterPluginBinding.getTextureRegistry());
    flutterState.startListening(this, flutterPluginBinding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (flutterState == null) {
      return;
    }
    flutterState.stopListening(binding.getBinaryMessenger());
    flutterState = null;
  }

  @Override
  public void initialize() {
    // Initialize logic if needed
  }

  @Override
  public void dispose() {
    if (flutterState != null) {
      flutterState.onDestroy();
    }
  }

  /** Inner class to hold Flutter-related state */
  static final class FlutterState {
    private final Context applicationContext;
    private final BinaryMessenger messenger;
    private final TextureRegistry textureRegistry;
    private final FlutterAssetsProvider assetsProvider;

    FlutterState(
        Context applicationContext,
        BinaryMessenger messenger,
        FlutterAssetsProvider assetProvider,
        FlutterAssetsProvider packageAssetProvider,
        TextureRegistry textureRegistry) {
      this.applicationContext = applicationContext;
      this.messenger = messenger;
      this.textureRegistry = textureRegistry;
      this.assetsProvider = assetProvider;
    }

    void startListening(VideoPlayerApi api, BinaryMessenger messenger) {
      VideoPlayerApi.setup(messenger, api);
    }

    void stopListening(BinaryMessenger messenger) {
      VideoPlayerApi.setup(messenger, null);
    }

    void onDestroy() {
      // clean up resources
    }

    interface FlutterAssetsProvider {
      String getAssetFilePathByName(String assetName);
    }
  }
}
