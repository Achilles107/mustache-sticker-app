package com.example.area61;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    private ModelRenderable modelRenderable;
    private Texture texture;

    private boolean isAdded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button recordButton = findViewById(R.id.recordButton);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Recording Started", Toast.LENGTH_SHORT).show();
            }
        });
        CustomArFragment customArFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        ModelRenderable.builder().setSource(this, R.raw.fox_face).build().thenAccept(modelRenderable1 -> {
            modelRenderable = modelRenderable1;
            modelRenderable.setShadowCaster(false);
            modelRenderable.setShadowReceiver(false);
        });

        Texture.builder().setSource(this, R.drawable.fox_face_mesh_texture).build().thenAccept(texture -> this.texture = texture);

        customArFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
        customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if (modelRenderable == null || texture == null)
                return;
            Frame frame = customArFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);
            for (AugmentedFace augmentedFace: augmentedFaces){
                if (isAdded){
                    return;
                }
                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);


                augmentedFaceNode.setFaceMeshTexture(texture);
                isAdded = true;
            }
        });
    }

}