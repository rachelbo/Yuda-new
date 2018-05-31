package com.example.myapplication.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.InboxActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.app.InboxModel;
import com.example.myapplication.app.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView user,name,email_main,type;
    private SessionManager session;
    private EditText searchView;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private TextView contact_user;
    private ImageView contact_pix,circleView;
    private Button send_message;
    private int PICK_IMAGE_REQUEST=1;
    private Bitmap bitmap_ga;

    private View promptsView;
    LayoutInflater inflater;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialogH;
    private String contact_id,contact_image,contact_online,contact_active,contact_username;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        user = rootView.findViewById(R.id.user);
        type = rootView.findViewById(R.id.type);
        name = rootView.findViewById(R.id.name);
        circleView = rootView.findViewById(R.id.circleView);
        email_main = rootView.findViewById(R.id.email_main);
        searchView = rootView.findViewById(R.id.searchView);
        alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(false);
        setUpDialog();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        session = new SessionManager(getContext());

        user.setText(session.getUsername());
        type.setText(session.getMarket_Table());
        name.setText(session.getName());
        email_main.setText(session.getEmail());

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String name = searchView.getText().toString().trim();
                    performSearch(name);
                    return true;
                }
                return false;
            }
        });
        Picasso.with(getContext()).load(AppConfig.URL_IMAGE+session.getImage()).into(circleView);

        circleView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showFileChooser();
            }

        });
        return rootView;
    }

    public void showFileChooser(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void performSearch(final String name){
        showDialog();
        String tag_string_req = "req_search_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEARCH_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        contact_id = jObj.getString("contact_id");
                        contact_image = jObj.getString("contact_image");
                        contact_online = jObj.getString("contact_online");
                        contact_active = jObj.getString("contact_active");
                        contact_username = jObj.getString("contact_username");

                        contact_user.setText(contact_username);
                        Picasso.with(getContext()).load(AppConfig.URL_IMAGE+contact_image).into(contact_pix);
                        alertDialogH.show();


                    }
                    else{
                        String errorMsg = jObj.getString("error_msg");
                        alertDialog.setMessage(errorMsg);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            Uri filePath = data.getData();
            try{
                bitmap_ga= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                circleView.setImageBitmap(bitmap_ga);
                uploadImage();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void setUpDialog() {
        inflater = getLayoutInflater();
        promptsView = inflater.inflate(R.layout.promt_dialog, null);
        contact_user = promptsView.findViewById(R.id.contact_user);
        contact_pix = promptsView.findViewById(R.id.contact_pix);
        send_message = promptsView.findViewById(R.id.send_message);

        alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        alertDialogH = alertDialogBuilder.create();
        alertDialogH.setCancelable(true);

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogH.cancel();
                Intent intent = new Intent(getContext(), InboxActivity.class);
                intent.putExtra("contact_id",contact_id);
                intent.putExtra("contact_image",contact_image);
                intent.putExtra("contact_online",contact_online);
                intent.putExtra("contact_active",contact_active);
                intent.putExtra("contact_username",contact_username);
                intent.putExtra("user_id",session.getId());
                intent.putExtra("username",session.getUsername());
                startActivity(intent);
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imagesBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imagesBytes,Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        StringRequest uploadImage = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD_IMAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    String image_name = jObj.getString("image_name");
                    if (success == 1) {
                        session.setImage(image_name+".jpg");
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                String image = getStringImage(bitmap_ga);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Image", image);
                params.put("user_id", session.getId());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(uploadImage);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Profile");
    }

}
