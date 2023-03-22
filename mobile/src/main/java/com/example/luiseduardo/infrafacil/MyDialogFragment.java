package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.MoneyTextWatcher.getCurrencySymbol;
import static com.example.luiseduardo.infrafacil.customAdapter.idjogo;
import static com.example.luiseduardo.infrafacil.customAdapter.imageuser;
import static com.example.luiseduardo.infrafacil.customAdapter.sUsername;
import static com.example.luiseduardo.infrafacil.customAdapter.totaladdon;
import static com.example.luiseduardo.infrafacil.customAdapter.totalplayers;
import static com.example.luiseduardo.infrafacil.customAdapter.totalrebuy;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.NumberFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDialogFragment extends DialogFragment {

    JSONParser jsonParser = new JSONParser();
    JSONObject object =null;
    private boolean PLAYERS = false;
    private static String URLUP = "http://futsexta.16mb.com/Poker/Poker_Edit_Players.php";
    private static String URTOTAIS = "http://futsexta.16mb.com/Poker/Poker_Get_Totais.php";
    private static String urlplayers = "http://futsexta.16mb.com/Poker/poker_get_playersjogo.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static String total = "0";
    private final Locale locale = Locale.getDefault();
    private String upname;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    Bitmap bitmap;
    ProgressDialog progressDialog ;
    boolean check = true;

    String GetImageNameFromEditText;

    String ImageNameFieldOnServer = "image_name" ;

    String ImagePathFieldOnServer = "image_path" ;

    String ImageUploadPathOnSever ="http://futsexta.16mb.com/Poker/capture_img_upload_to_server.php" ;
    //public static String  sUsername,idplayer,idjogo,rebuy, addon, valor, vlrebuy,vladdon,vlentrada,totalrebuy,totaladdon,totalplayers,imageuser;

    private  List<PlayersListView> list;

/*
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert Dialog");
        builder.setMessage("Teste de Alert");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();

    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.custom_alertnewplayer,container);

        //final PlayersListView city = list.get(position);

        CircleImageView img = (CircleImageView) view.findViewById(R.id.imgaddplayers);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((Poker) getActivity()).turnOffFrag();
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    Toast.makeText(getContext(), "sem permissão", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });

        if(imageuser.equals("0")){
            Picasso.with(getActivity()).load("http://futsexta.16mb.com/Poker/imgplayer/useredit.png").into(img);
        }else{
            if(Poker.reload){
                Picasso.with(getActivity()).load(imageuser).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(img);

            }else{
                Picasso.with(getActivity()).load(imageuser).into(img);
            }


            //Picasso.with(getContext()).load(data.get(pos).getFeed_thumb_image()).memoryPolicy(MemoryPolicy.NO_CACHE).into(img);
        }
        TextView action = (TextView) view.findViewById(R.id.tvactionplayer);
        action.setText("Editando Jogador");

        final TextView nameview = view.findViewById(R.id.tvname);
        nameview.setText("Editar Player");

        EditText name = (EditText) view.findViewById(R.id.ednomePlayer);
        name.setText(sUsername);
        name.setSelection(name.getText().length());
        name.setEnabled(true);

        LinearLayout LButtons = (LinearLayout) view.findViewById(R.id.LayoutButtonsPlayers);
        LButtons.setVisibility(View.VISIBLE);


        Button btncancel = (Button) view.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Poker.reload = false;
                ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(name.getWindowToken(), 0);
                dismiss();
                //((Poker) getActivity()).turnOffFrag();
            }
        });

        Button btnsave = (Button) view.findViewById(R.id.btnSave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImageNameFromEditText = name.getText().toString();
                sUsername = String.valueOf(name.getText());

                if(bitmap != null){
                    ImageUploadToServerFunction();
                    Poker.reload = true;
                    ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(name.getWindowToken(), 0);
                    new  UpdatePlayer().execute();
                }else{
                    Poker.reload = false;
                    ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(name.getWindowToken(), 0);
                    new  UpdatePlayer().execute();
                }
            }
        });
        return (view);


    }
/*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final EditText editText = view.findViewById(R.id.ednomePlayer);

        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("email")))
            editText.setText(getArguments().getString("email"));

        Button btnDone = view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //DialogListener dialogListener = (DialogListener) getActivity();
                //dialogListener.onFinishEditDialog(editText.getText().toString());
                dismiss();
            }
        });
    }
*/
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public void show(FragmentTransaction ft, String dialog) {
    }

    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    //Bitmap
                    bitmap = bundle.getParcelable("data");
                    CircleImageView imageView = (CircleImageView) getView().findViewById(R.id.imgaddplayers);
                    imageView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

   class UpdatePlayer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            try {

                List params = new ArrayList();

                params.add(new BasicNameValuePair("id", customAdapter.idplayer));
                params.add(new BasicNameValuePair("rebuy", customAdapter.rebuy));
                params.add(new BasicNameValuePair("addon", customAdapter.addon));
                params.add(new BasicNameValuePair("valor", customAdapter.valor));
                params.add(new BasicNameValuePair("nome", sUsername));

                JSONObject newjson = jsonParser.makeHttpRequest(URLUP, "POST",
                        params);

                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Editar successo!", newjson.toString());
                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Não Atualizada", newjson.getString(TAG_MESSAGE));
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            dismiss();
        }

    }

    class GetTotais extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {


            List params = new ArrayList();
            params.add(new BasicNameValuePair("id", idjogo));

            JSONObject json = jsonParser.makeHttpRequest(URTOTAIS, "POST",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("jogo");

                    for (int i = 0; i < eventDetails.length(); i++) {
                        object = eventDetails.getJSONObject(i);
                        String tt = object.getString("total");
                        String ttr = object.getString("totalrebuy");
                        String tta = object.getString("totaladdon");
                        String ttp = object.getString("totalplayers");


                        total = tt;
                        totalplayers = ttp;
                        totalrebuy = ttr;
                        totaladdon = tta;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(String file_url) {
            //super.onPostExecute(result);
            BigDecimal parsed = parseToBigDecimal(total);
            String formatted;
            formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

            Poker.vltotaljogo.setText(formatted);
            Poker.ttplayers.setText(totalplayers);
            Poker.ttrebuy.setText(totalrebuy);
            Poker.ttaddon.setText(totaladdon);


            int vl = (int) Double.parseDouble(total);

            int pri = (vl / 100) * 50;
            int seg = (vl / 100) * 30;
            int ter = (vl / 100) * 20;

            BigDecimal prim = parseToBigDecimal(String.valueOf(pri));
            String sprimeiro;
            sprimeiro = NumberFormat.getCurrencyInstance(locale).format(prim);
            Poker.primeiro.setText(sprimeiro);

            BigDecimal segu = parseToBigDecimal(String.valueOf(seg));
            String ssegundoo;
            ssegundoo = NumberFormat.getCurrencyInstance(locale).format(segu);
            Poker.segundo.setText(String.valueOf(ssegundoo));

            BigDecimal terc = parseToBigDecimal(String.valueOf(ter));
            String sterceiro;
            sterceiro = NumberFormat.getCurrencyInstance(locale).format(terc);
            Poker.terceiro.setText(String.valueOf(sterceiro));

        }
    }

    private BigDecimal parseToBigDecimal(String value) {
        String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());

        String cleanString = value.replaceAll(replaceable, "");

        try {
            return new BigDecimal(cleanString).setScale(
                    2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        } catch (NumberFormatException e) {
            //ao apagar todos valores de uma só vez dava erro
            //Com a exception o valor retornado é 0.00
            return new BigDecimal(0);

        }
    }

    // Upload captured image online on server function.
    public void  ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(getActivity(),"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                new  UpdatePlayer().execute();
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                //Toast.makeText(getActivity(),string1,Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                //ImageViewHolder.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
                HashMapParams.put("id", customAdapter.idplayer);
                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        ((Poker) getActivity()).turnOffFrag();
    }
}
