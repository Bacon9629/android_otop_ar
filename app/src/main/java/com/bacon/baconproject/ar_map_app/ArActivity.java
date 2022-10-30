package com.bacon.baconproject.ar_map_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;

import com.bacon.baconproject.ar_map_app.R;

public class ArActivity extends AppCompatActivity implements
        FragmentOnAttachListener,
        BaseArFragment.OnTapArPlaneListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener {


    private Context context;
    private ViewRenderable img_view;
    private Anchor anchor;
    private Node titleNode;
    private Renderable model;
    private TransformableNode transformableNode;

    private TextView left_bt, right_bt;
    private View ar_root;
    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        context = this;
        getSupportFragmentManager().addFragmentOnAttachListener(this);

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.ar_fragment, ArFragment.class, null)
                        .commit();
            }
        }

        loadModels();

    }



    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.ar_fragment) {
            arFragment = (ArFragment) fragment;
            arFragment.setOnSessionConfigurationListener(this);
            arFragment.setOnViewCreatedListener(this);
            arFragment.setOnTapArPlaneListener(this);
        }
    }

    @Override
    public void onSessionConfiguration(Session session, Config config) {
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
        }
    }


    @Override
    public void onViewCreated(ArSceneView arSceneView) {

        arFragment.setOnViewCreatedListener(null);

        // Fine adjust the maximum frame rate
        arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);
    }

    public void loadModels() {
        WeakReference<ArActivity> weakActivity = new WeakReference<>((ArActivity) context);

        ar_root = LayoutInflater.from(this).inflate(R.layout.ar_2d, null, false);

        left_bt = ar_root.findViewById(R.id.ar_left_bt);
        right_bt = ar_root.findViewById(R.id.ar_right_bt);
        left_bt.setOnClickListener(on_click);
        right_bt.setOnClickListener(on_click);

        ImageView ar_img = ar_root.findViewById(R.id.ar_img);
        AnimationDrawable ani =
                (AnimationDrawable)getResources().getDrawable(R.drawable.ar_wait_animation);

        ar_img.setImageDrawable(ani);
        ani.start();


        ViewRenderable.builder()
                .setView(context, ar_root)
                .build()
                .thenAccept(viewRenderable -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
//                        first show img
                        activity.img_view = viewRenderable;
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(context, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });
    }


    View.OnClickListener on_click = view -> {

        ImageView ar_img = ar_root.findViewById(R.id.ar_img);
        if (view.getTag().equals("left")){
            AnimationDrawable ani =
                    (AnimationDrawable)getResources().getDrawable(R.drawable.ar_water_animation);
            ar_img.setImageDrawable(ani);
            ani.start();
        }else{
//            Toast.makeText(context, "abc", Toast.LENGTH_SHORT).show();
            AnimationDrawable ani =
                    (AnimationDrawable)getResources().getDrawable(R.drawable.ar_mallet_animation);
            ar_img.setImageDrawable(ani);
            ani.start();
        }


//        if (view.getTag().toString().equals("left")){
//            view.setForeground(getDrawable(R.drawable.bt_clean_down));
//        }else{
//            view.setForeground(getDrawable(R.drawable.bt_mallet_down));
//        }


//        view.getTag().toString().equals("left")
        ImageButton restart_bt = ar_root.findViewById(R.id.ar_restart);
        if (!restart_bt.hasOnClickListeners()){
            restart_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnimationDrawable ani =
                            (AnimationDrawable)getResources().getDrawable(R.drawable.ar_wait_animation);

                    ar_img.setImageDrawable(ani);
                    ani.start();
                }
            });
        }

    };

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {

        if (img_view == null) {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Toast.makeText(context, "Touch plane", Toast.LENGTH_SHORT).show();
        }

        if (titleNode != null){
            return;
        }

        // Create the Anchor.
        anchor = hitResult.createAnchor();
//        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

//        this.anchor = anchor;

        // Create the transformable model and add it to the anchor.  3D
//        TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
//        model.setParent(anchorNode);
//        model.setRenderable(this.model)
//                .animate(true).start();
//        model.select();
//        this.transformableNode = model;


        titleNode = new Node();
        titleNode.setParent(anchorNode);
        titleNode.setEnabled(false);
        titleNode.setLocalPosition(new Vector3(0.0f, 0.0f, 0.0f));
        titleNode.setRenderable(img_view);
        titleNode.setEnabled(true);
    }

}