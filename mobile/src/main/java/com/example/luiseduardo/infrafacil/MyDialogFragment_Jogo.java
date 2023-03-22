package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.MoneyTextWatcher.getCurrencySymbol;
import static com.example.luiseduardo.infrafacil.Poker_main.datejogo;
import static com.example.luiseduardo.infrafacil.Poker_main.descrijogo;
import static com.example.luiseduardo.infrafacil.Poker_main.sImg;
import static com.example.luiseduardo.infrafacil.Poker_main.sQtdaddon;
import static com.example.luiseduardo.infrafacil.Poker_main.sQtdentrada;
import static com.example.luiseduardo.infrafacil.Poker_main.sQtdrebuy;
import static com.example.luiseduardo.infrafacil.Poker_main.sVldaddon;
import static com.example.luiseduardo.infrafacil.Poker_main.sVldentrada;
import static com.example.luiseduardo.infrafacil.Poker_main.sVldrebuy;
import static com.example.luiseduardo.infrafacil.Poker_main.ssData;
import static com.example.luiseduardo.infrafacil.customAdapter.idjogo;
import static com.example.luiseduardo.infrafacil.customAdapter.totaladdon;
import static com.example.luiseduardo.infrafacil.customAdapter.totalplayers;
import static com.example.luiseduardo.infrafacil.customAdapter.totalrebuy;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDialogFragment_Jogo extends DialogFragment {

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    JSONObject object =null;
    private static String URTOTAIS = "http://futsexta.16mb.com/Poker/Poker_Get_Totais.php";
    private static String URLUPJOGO = "http://futsexta.16mb.com/Poker/Poker_Edit_Jogo.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static String total = "0";
    private final Locale locale = Locale.getDefault();
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static String Mes,Dia,Ano;
    Bitmap bitmap;
    ProgressDialog progressDialog ;
    boolean check = true;
    String GetImageNameFromEditText;
    String ImageNameFieldOnServer = "image_name" ;
    String ImagePathFieldOnServer = "image_path" ;
    String ImageUploadPathOnSever ="http://futsexta.16mb.com/Poker/capture_img_jogo.php" ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_newpoker,container);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView editTextDate = (TextView) view.findViewById(R.id.editTextDate);
        String ds1  = datejogo;
        SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            datejogo = sdf2.format(sdf1.parse(ds1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editTextDate.setText(datejogo);
        EditText editvalorentrada = (EditText) view.findViewById(R.id.edvalorentrada);
        EditText editvalorrebuy = (EditText) view.findViewById(R.id.edvalorrebuy);
        EditText editvaloraddon = (EditText) view.findViewById(R.id.edvaloraddon);
        EditText editqtdentrada = (EditText) view.findViewById(R.id.edqtdentrada);
        EditText editqtdrebuy = (EditText) view.findViewById(R.id.edqtdrebuy);
        EditText editqtdaddon = (EditText) view.findViewById(R.id.edqtdaddon);
        TextView textViewNumJogo = (TextView) view.findViewById(R.id.textViewNumJogo);
        editvalorentrada.addTextChangedListener(new Poker_main.MoneyTextWatcher(editvalorentrada));
        editvalorrebuy.addTextChangedListener(new Poker_main.MoneyTextWatcher(editvalorrebuy));
        editvaloraddon.addTextChangedListener(new Poker_main.MoneyTextWatcher(editvaloraddon));
        //editqtdentrada.addTextChangedListener(new Poker_main.MilharTextWatcher(editqtdentrada));
        editqtdentrada.addTextChangedListener(Poker_main.MaskEditUtil.mask(editqtdentrada, Poker_main.MaskEditUtil.FORMAT_MILHAR));
        //editqtdrebuy.addTextChangedListener(new Poker_main.MilharTextWatcher(editqtdrebuy));
        editqtdrebuy.addTextChangedListener(Poker_main.MaskEditUtil.mask(editqtdrebuy, Poker_main.MaskEditUtil.FORMAT_MILHAR));
        //editqtdaddon.addTextChangedListener(new Poker_main.MilharTextWatcher(editqtdaddon));
        editqtdaddon.addTextChangedListener(Poker_main.MaskEditUtil.mask(editqtdaddon, Poker_main.MaskEditUtil.FORMAT_MILHAR));
        editvalorentrada.setText(sVldentrada);
        editvalorrebuy.setText(sVldrebuy);
        editvaloraddon.setText(sVldaddon);
        editqtdentrada.setText(sQtdentrada);
        editqtdrebuy.setText(sQtdrebuy);
        editqtdaddon.setText(sQtdaddon);
        textViewNumJogo.setText(Poker_main.idjogo);
        tvTitle.setText("Editando o Jogo");
        CircleImageView img = view.findViewById(R.id.imgjogo);
        if(sImg.equals("0")){
            Picasso.with(getActivity()).load("http://futsexta.16mb.com/Poker/imgjogo/default.png").into(img);
        }else{
            if(Poker.reload){
                Picasso.with(getActivity()).load(sImg).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(img);

            }else{
                Picasso.with(getActivity()).load(sImg).into(img);
            }
        }
        CircleImageView imgPhoto = view.findViewById(R.id.imgfhoto);
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    Toast.makeText(getActivity(), "sem permissão", Toast.LENGTH_SHORT).show();
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        EditText ednome = view.findViewById(R.id.namejogo);
        ednome.setText(descrijogo);
        ednome.setSelection(ednome.getText().length());
        ednome.setEnabled(true);
        ImageButton btndata = view.findViewById(R.id.btn_date);
        btndata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                //showDatePickerDialog();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if(dayOfMonth < 10){
                                    Dia = "0"+ (dayOfMonth);
                                }else {
                                    Dia = String.valueOf(dayOfMonth);
                                }
                                if(monthOfYear < 10){
                                    Mes = "0"+ (monthOfYear + 1);
                                }else{
                                    Mes = String.valueOf(monthOfYear + 1);
                                }
                                Poker_main.sData = String.valueOf(year)+Mes+Dia;
                                //new Poker_main.GetDados_jogos_from_date().execute();
                                String ds1  = String.valueOf(year)+"-"+Mes+"-"+Dia;

                                SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    datejogo = sdf2.format(sdf1.parse(ds1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                editTextDate.setText(datejogo);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        LinearLayout LButtons = (LinearLayout) view.findViewById(R.id.LayoutButtons);
        LButtons.setVisibility(View.VISIBLE);
        Button btncancel = (Button) view.findViewById(R.id.btnCancelarnjogo);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Poker.reload = false;
                ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(ednome.getWindowToken(), 0);
                dismiss();
                //((Poker) getActivity()).turnOffFrag();
            }
        });
        Button btnsave = (Button) view.findViewById(R.id.btnSalvarjogo);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImageNameFromEditText = ednome.getText().toString();
                //sUsername = String.valueOf(name.getText());

                Boolean dt = false, nm = false, vle = false,vlr = false,vla = false,qtde = false,qtdr = false,qtda = false;
                //TextView dateEditText = (TextView) view.findViewById(R.id.editTextDate);
                ImageButton btndataa = (ImageButton) view.findViewById(R.id.btn_date);

                ssData = editTextDate.getText().toString();
                if (ssData.matches("")) {
                    Toast.makeText(getActivity(), "Favor Selecionar a data do Jogo", Toast.LENGTH_SHORT).show();
                    btndataa.performClick();
                    dt = false;
                }else{

                    dt = true;
                }

                //AutoCompleteTextView usernameEditText = (AutoCompleteTextView) view.findViewById(R.id.namejogo);
                descrijogo = ednome.getText().toString();
                if (descrijogo.matches("")) {
                    Toast.makeText(getActivity(), "Favor Preencher o nome do Jogo", Toast.LENGTH_SHORT).show();
                    ednome.requestFocus();
                    nm = false;
                }else{
                    nm = true;
                }
                //TextView Vldentrada = (TextView) view.findViewById(R.id.edvalorentrada);
                sVldentrada = editvalorentrada.getText().toString();

                String replaceable = String.format("[%s\\s]", Poker_new.MoneyTextWatcher.getCurrencySymbol());
                sVldentrada = sVldentrada.replaceAll(replaceable, "");
                sVldentrada = sVldentrada.replaceAll(",", "");

                if ( (sVldentrada.matches("")) || (sVldentrada.matches("000") )) {
                    Toast.makeText(getActivity(), "Favor preencher o valor da Entrada", Toast.LENGTH_SHORT).show();
                    editvalorentrada.requestFocus();//dialog.dismiss();
                    vle = false;
                }else{
                    vle = true;
                }
                //EditText Qtdentrada = (EditText) view.findViewById(R.id.edqtdentrada);
                sQtdentrada = editqtdentrada.getText().toString();
                sQtdentrada = Poker_main.MaskEditUtil.unmask(editqtdentrada.getText().toString());
                if ( (sQtdentrada.matches("")) || (sQtdentrada.matches("0") )) {
                    //dialog.dismiss();
                    Toast.makeText(getActivity(), "Favor preencher a quantidade de ficha da Entrada", Toast.LENGTH_SHORT).show();
                    editqtdentrada.requestFocus();
                    qtde = false;
                }else{
                    qtde = true;
                }

                //TextView Vldrebuy = (TextView) view.findViewById(R.id.edvalorrebuy);
                sVldrebuy = editvalorrebuy.getText().toString();
                sVldrebuy = sVldrebuy.replaceAll(replaceable, "");
                sVldrebuy = sVldrebuy.replaceAll(",", "");
                if ((sVldrebuy.matches("")) || (sVldrebuy.matches("000")) ) {
                    Toast.makeText(getActivity(), "Favor preencher o valor da Rebuy", Toast.LENGTH_SHORT).show();
                    editvalorrebuy.requestFocus();
                    vlr = false;
                }else{
                    vlr = true;
                }
                //TextView Qtdrebuy = (TextView) view.findViewById(R.id.edqtdrebuy);
                sQtdrebuy = editqtdrebuy.getText().toString();
                sQtdrebuy = Poker_main.MaskEditUtil.unmask(editqtdrebuy.getText().toString());
                if ( (sQtdrebuy.matches("")) || (sQtdrebuy.matches("0") )) {
                    Toast.makeText(getActivity(), "Favor preencher a quantidade de ficha do Rebuy", Toast.LENGTH_SHORT).show();
                    editqtdrebuy.requestFocus();
                    qtdr = false;
                }else{
                    qtdr = true;
                }

                //TextView Vldaddon = (TextView) view.findViewById(R.id.edvaloraddon);
                sVldaddon = editvaloraddon.getText().toString();
                sVldaddon = sVldaddon.replaceAll(replaceable, "");
                sVldaddon = sVldaddon.replaceAll(",", "");
                if ((sVldaddon.matches("")) || (sVldaddon.matches("000")) ) {
                    Toast.makeText(getActivity(), "Favor preencher o valor da Addon", Toast.LENGTH_SHORT).show();
                    editvaloraddon.requestFocus();
                    vla = false;
                }else{
                    vla = true;
                }

                //TextView Qtdaddon = (TextView) view.findViewById(R.id.edqtdaddon);
                sQtdaddon = editqtdaddon.getText().toString();
                sQtdaddon = Poker_main.MaskEditUtil.unmask(editqtdaddon.getText().toString());
                if ( (sQtdaddon.matches("")) || (sQtdaddon.matches("0") )) {
                    Toast.makeText(getActivity(), "Favor preencher a quantidade de ficha do Addon", Toast.LENGTH_SHORT).show();
                    editqtdaddon.requestFocus();
                    qtda = false;
                }else{
                    qtda = true;
                }

                if((dt) && (nm) && (vle)&& (vlr)&& (vla)&& (qtde)&& (qtdr)&& (qtda)){

                    //adapterListView.notifyDataSetChanged();
                    if(bitmap != null){
                        ImageUploadToServerFunction();
                        Poker.reload = true;
                        ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(ednome.getWindowToken(), 0);

                    }else{
                        Poker.reload = false;
                        ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(ednome.getWindowToken(), 0);
                        new UpdatetJogo().execute();
                    }


                    //View vieww = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        //inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                //closeKeyboard();
                //InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(dialog.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });
        return (view);
    }
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
                    CircleImageView imageView = (CircleImageView) getView().findViewById(R.id.imgjogo);
                    imageView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


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

                new  UpdatetJogo().execute();
                progressDialog.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
                HashMapParams.put("id", Poker_main.idjogo);
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
    class UpdatetJogo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Atualizando Jogo");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                List params = new ArrayList();

                params.add(new BasicNameValuePair("id", Poker_main.idjogo));
                params.add(new BasicNameValuePair("Descricao", Poker_main.descrijogo));
                params.add(new BasicNameValuePair("Data", Poker_main.sData));
                params.add(new BasicNameValuePair("vlentrada", Poker_main.sVldentrada));
                params.add(new BasicNameValuePair("qtdfichaentrada", Poker_main.sQtdentrada));
                params.add(new BasicNameValuePair("vlrebuy", Poker_main.sVldrebuy));
                params.add(new BasicNameValuePair("qtdficharebuy", Poker_main.sQtdrebuy));
                params.add(new BasicNameValuePair("vladdon", Poker_main.sVldaddon));
                params.add(new BasicNameValuePair("qtdfichaaddon", Poker_main.sQtdaddon));

                JSONObject json = jsonParser.makeHttpRequest(URLUPJOGO, "POST",
                        params);
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("successo!", json.toString());
                    return json.getString(TAG_MESSAGE);

                } else {
                    Log.d("Jogo não Atualizado", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
            }
            //new GetDados_jogos().execute();
            dismiss();
        }


    }
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        ((Poker_main) getActivity()).turnOffFrag();
    }
}
