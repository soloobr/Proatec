package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.Poker_new.MoneyTextWatcher.getCurrencySymbol;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
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

public class Poker_new extends AppCompatActivity implements
        View.OnClickListener {
    Button btncanceljogo, btnsavejogo;
    ImageButton btndata;
    CircleImageView img;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static String Mes,Dia,Ano,sData,sSdata;
    TextView dateEditText,Nextid;
    public static String datejogo, nextid ;
    private EditText editvalorentrada,editvalorrebuy,editvaloraddon, editqtdentrada,editqtdrebuy,editqtdaddon;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String GETINFO_URL = "http://futsexta.16mb.com/Poker/Poker_insert_Jogo.php";
    private static String GETNEXTID_URL = "http://futsexta.16mb.com/Poker/Poker_next_id_Jogo.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ID = "id";

    String  sUsername, ssData, sVldentrada,sVldrebuy,sVldaddon,sQtdentrada,sQtdrebuy,sQtdaddon;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public static String newidjogo;
    Bitmap bitmap;
    ProgressDialog progressDialog ;
    boolean check = true;

    String GetImageNameFromEditText;

    String ImageNameFieldOnServer = "image_name" ;

    String ImagePathFieldOnServer = "image_path" ;
    String mask, old;

    String ImageUploadPathOnSever ="http://futsexta.16mb.com/Poker/capture_img_jogo.php";
    boolean isUpdating;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpoker);


        img = (CircleImageView) findViewById(R.id.imgaddplayers);
        Nextid = (TextView)  findViewById(R.id.textViewNumJogo);
        btndata =  (ImageButton) findViewById(R.id.btn_date);
        btncanceljogo = (Button) findViewById(R.id.btnCancelarnjogo);
        btnsavejogo = (Button) findViewById(R.id.btnSalvarjogo);
        dateEditText = (TextView) findViewById(R.id.editTextDate);
        editvalorentrada = (EditText) findViewById(R.id.edvalorentrada);
        editvalorrebuy = (EditText) findViewById(R.id.edvalorrebuy);
        editvaloraddon = (EditText) findViewById(R.id.edvaloraddon);
        editqtdentrada = (EditText) findViewById(R.id.edqtdentrada);
        editqtdrebuy = (EditText) findViewById(R.id.edqtdrebuy);
        editqtdaddon = (EditText) findViewById(R.id.edqtdaddon);

        editvalorentrada.addTextChangedListener(new Poker_new.MoneyTextWatcher(editvalorentrada));
        editvalorrebuy.addTextChangedListener(new Poker_new.MoneyTextWatcher(editvalorrebuy));
        editvaloraddon.addTextChangedListener(new Poker_new.MoneyTextWatcher(editvaloraddon));

        editqtdentrada.addTextChangedListener(MaskEditUtil.mask(editqtdentrada, MaskEditUtil.FORMAT_CPF));
        editqtdrebuy.addTextChangedListener(MaskEditUtil.mask(editqtdrebuy, MaskEditUtil.FORMAT_CPF));
        editqtdaddon.addTextChangedListener(MaskEditUtil.mask(editqtdaddon, MaskEditUtil.FORMAT_CPF));


        LinearLayout LButtons = (LinearLayout) findViewById(R.id.LayoutButtons);
        LButtons.setVisibility(View.VISIBLE);

        new GetNextIDJogo().execute();

        btncanceljogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                finish();
            }
        });

        btnsavejogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView usernameEditText = (AutoCompleteTextView) findViewById(R.id.namejogo);
                sUsername = usernameEditText.getText().toString();
                if (sUsername.matches("")) {
                    Toast.makeText(Poker_new.this, "Favor Preencher o nome do Jogo", Toast.LENGTH_SHORT).show();
                    usernameEditText.requestFocus();
                    return;
                }else{
                    GetImageNameFromEditText = sUsername;
                }


                TextView dateEditText = (TextView) findViewById(R.id.editTextDate);
                ssData = dateEditText.getText().toString();
                if (ssData.matches("")) {
                    Toast.makeText(Poker_new.this, "Favor Selecionar a data do Jogo", Toast.LENGTH_SHORT).show();
                    btndata.performClick();
                    return;
                }

                TextView Vldentrada = (TextView) findViewById(R.id.edvalorentrada);
                sVldentrada = Vldentrada.getText().toString();

                String replaceable = String.format("[%s\\s]", getCurrencySymbol());
                sVldentrada = sVldentrada.replaceAll(replaceable, "");
                sVldentrada = sVldentrada.replaceAll(",", "");

                if ( (sVldentrada.matches("")) || (sVldentrada.matches("000") )) {
                    Toast.makeText(Poker_new.this, "Favor preencher o valor da Entrada", Toast.LENGTH_SHORT).show();
                    Vldentrada.requestFocus();
                    return;
                }
                TextView Qtdentrada = (TextView) findViewById(R.id.edqtdentrada);
                sQtdentrada = Qtdentrada.getText().toString();
                if ( (sQtdentrada.matches("")) || (sQtdentrada.matches("0") )) {
                    Toast.makeText(Poker_new.this, "Favor preencher a quantidade de ficha da Entrada", Toast.LENGTH_SHORT).show();
                    Qtdentrada.requestFocus();
                    return;
                }

                TextView Vldrebuy = (TextView) findViewById(R.id.edvalorrebuy);
                sVldrebuy = Vldrebuy.getText().toString();
                sVldrebuy = sVldrebuy.replaceAll(replaceable, "");
                sVldrebuy = sVldrebuy.replaceAll(",", "");
                if ((sVldrebuy.matches("")) || (sVldrebuy.matches("000")) ) {
                    Toast.makeText(Poker_new.this, "Favor preencher o valor da Rebuy", Toast.LENGTH_SHORT).show();
                    Vldrebuy.requestFocus();
                    return;
                }
                TextView Qtdrebuy = (TextView) findViewById(R.id.edqtdrebuy);
                sQtdrebuy = Qtdrebuy.getText().toString();
                if ( (sQtdrebuy.matches("")) || (sQtdrebuy.matches("0") )) {
                    Toast.makeText(Poker_new.this, "Favor preencher a quantidade de ficha do Rebuy", Toast.LENGTH_SHORT).show();
                    Qtdrebuy.requestFocus();
                    return;
                }

                TextView Vldaddon = (TextView) findViewById(R.id.edvaloraddon);
                sVldaddon = Vldaddon.getText().toString();
                sVldaddon = sVldaddon.replaceAll(replaceable, "");
                sVldaddon = sVldaddon.replaceAll(",", "");
                if ((sVldaddon.matches("")) || (sVldaddon.matches("000")) ) {
                    Toast.makeText(Poker_new.this, "Favor preencher o valor da Addon", Toast.LENGTH_SHORT).show();
                    Vldaddon.requestFocus();
                    return;
                }

                TextView Qtdaddon = (TextView) findViewById(R.id.edqtdaddon);
                sQtdaddon = Qtdaddon.getText().toString();
                if ( (sQtdaddon.matches("")) || (sQtdaddon.matches("0") )) {
                    Toast.makeText(Poker_new.this, "Favor preencher a quantidade de ficha do Addon", Toast.LENGTH_SHORT).show();
                    Qtdaddon.requestFocus();
                    return;
                }



                new InsertJogo().execute();

                LoadPhotos();
                closeKeyboard();
            }
        });


    }

    @SuppressLint("ResourceAsColor")
    public void LoadPhoto(View view) {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            Toast.makeText(this, "sem permissão", Toast.LENGTH_SHORT).show();
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }


    @SuppressLint("ResourceAsColor")
    public void onClick(View view) {

    }
    @SuppressLint("ResourceAsColor")
    public void onClickDATA(View v) {
        //Toast.makeText(this, "Date", Toast.LENGTH_SHORT).show();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(Poker_new.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ;
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
                        sData = String.valueOf(year)+Mes+Dia;
                        //new Poker_main.GetDados_jogos_from_date().execute();
                        String ds1  = String.valueOf(year)+"-"+Mes+"-"+Dia;

                        SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            datejogo = sdf2.format(sdf1.parse(ds1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dateEditText.setText(datejogo);  ;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @SuppressLint("ResourceAsColor")
    public void onClickSAVE(View v) {


                    /*Poker.reload = false;
            ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(name.getWindowToken(), 0);*/

    }
    class InsertJogo extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Poker_new.this);
            pDialog.setMessage("Criando Jogo");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {

            int success;
            String nweid;
            try {
                // Building Parameters
                List params = new ArrayList();

                //params.add(new BasicNameValuePair("id", Data_Local));
                params.add(new BasicNameValuePair("Descricao", sUsername));
                params.add(new BasicNameValuePair("Data", sData));
                params.add(new BasicNameValuePair("vlentrada", sVldentrada));
                params.add(new BasicNameValuePair("qtdfichaentrada", sQtdentrada));
                params.add(new BasicNameValuePair("vlrebuy", sVldrebuy));
                params.add(new BasicNameValuePair("qtdficharebuy", sQtdrebuy));
                params.add(new BasicNameValuePair("vladdon", sVldaddon));
                params.add(new BasicNameValuePair("qtdfichaaddon", sQtdaddon));
                //params.add(new BasicNameValuePair("image_path", sQtdaddon));

                //Log.d("Debug!", "starting");

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(GETINFO_URL, "POST",
                        params);

                success = json.getInt(TAG_SUCCESS);
                nweid = json.getString(TAG_ID);
                if (success == 1) {
                    Log.d("Jogo Atualizado", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                } else if (success == 0) {
                    Log.d("successo jogo criado!", json.toString());
                    newidjogo = nweid;
                    return json.getString(TAG_MESSAGE);
                } else{
                    Log.d("Jogo não Criado", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {

                if(file_url.equals("Jogo Inserido com sucesso!")){
                    //LoadPhotos();
                    // if(bitmap != null){

                    //   ImageUploadToServerFunctionNewGame();
                    // }

                    Toast.makeText(Poker_new.this, file_url, Toast.LENGTH_LONG).show();
                };
            }
            //new Poker_main.GetDados_jogos();
            final Intent data = new Intent();
            //intent.putExtra("someKey", someData);
            //Intent intent=new Intent();
            data.putExtra("ID", newidjogo);
            //intent.putExtra(,);
            setResult(2,data);
            finish();
        }


    }
    class GetNextIDJogo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            int success;
            //String nextid;
            try {
                List params = new ArrayList();
                params.add(new BasicNameValuePair("id", "0"));

                JSONObject json = jsonParser.makeHttpRequest(GETNEXTID_URL, "POST",
                        params);

                success = json.getInt(TAG_SUCCESS);
                nextid = json.getString(TAG_ID);
                if (success == 1) {
                    Log.d("Next Jogo", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                } else{
                    Log.d("Not Next Jogo", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
           // pDialog.dismiss();
            if (file_url != null) {

                if(file_url.equals("Next Jogo")){
                    Nextid.setText(nextid);
                };
            }
        }


    }
    public static class MoneyTextWatcher implements TextWatcher {
        private WeakReference<EditText> editTextWeakReference;
        private final Locale locale = Locale.getDefault();

        public MoneyTextWatcher(EditText editText) {
            this.editTextWeakReference = new WeakReference<>(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText editText = editTextWeakReference.get();
            if (editText == null) return;
            editText.removeTextChangedListener(this);

            BigDecimal parsed = parseToBigDecimal(editable.toString());
            String formatted;
            formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

            //Remove o símbolo da moeda e espaçamento pra evitar bug
            //String replaceable = String.format("[%s\\s]", getCurrencySymbol());
            //String cleanString = formatted.replaceAll(replaceable, "");

            //editText.setText(cleanString);
            editText.setText(formatted);
            //editText.setSelection(cleanString.length());
            editText.setSelection(formatted.length());
            editText.addTextChangedListener(this);
        }


        public void afterTextChangeBanco(Editable editable) {
            EditText editText = editTextWeakReference.get();
            if (editText == null) return;
            editText.removeTextChangedListener(this);

            BigDecimal parsed = parseToBigDecimal(editable.toString());
            String formatted;
            formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

            //Remove o símbolo da moeda e espaçamento pra evitar bug
            String replaceable = String.format("[%s\\s]", getCurrencySymbol());
            String cleanString = formatted.replaceAll(replaceable, "");

            editText.setText(cleanString);
            //editText.setText(formatted);
            editText.setSelection(cleanString.length());
            //editText.setSelection(formatted.length());
            editText.addTextChangedListener(this);
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

        public static String formatPrice(String price) {
            //Ex - price = 2222
            //retorno = 2222.00
            DecimalFormat df = new DecimalFormat("0.00");
            return String.valueOf(df.format(Double.valueOf(price)));

        }

        public static String formatTextPrice(String price) {
            //Ex - price = 3333.30
            //retorna formato monetário em Br = 3.333,30
            //retorna formato monetário EUA: 3,333.30
            //retornar formato monetário de alguns países europeu: 3 333,30
            BigDecimal bD = new BigDecimal(formatPriceSave(formatPrice(price)));
            String newFormat = null;
            newFormat = String.valueOf(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(bD));
            String replaceable = String.format("[%s]", getCurrencySymbol());
            return newFormat.replaceAll(replaceable, "");

        }

        static String formatPriceSave(String price) {
            //Ex - price = $ 5555555
            //return = 55555.55 para salvar no banco de dados
            String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());
            String cleanString = price.replaceAll(replaceable, "");
            StringBuilder stringBuilder = new StringBuilder(cleanString.replaceAll(" ", ""));

            return String.valueOf(stringBuilder.insert(cleanString.length() - 2, '.'));

        }

        static String formatPriceSaveBanco(String price) {
            //Ex - price = $ 5555555
            //return = 55555.55 para salvar no banco de dados
            String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());
            String cleanString = price.replaceAll(replaceable, "");
            StringBuilder stringBuilder = new StringBuilder(cleanString.replaceAll(" ", ""));

            return String.valueOf(stringBuilder.insert(cleanString.length() - 2, '.'));



        }

        public static String getCurrencySymbol() {
            return NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol();
        }
    }
    private void closeKeyboard(){
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
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
                    CircleImageView imageView = (CircleImageView) this.findViewById(R.id.imgjogo);
                    imageView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
    public void  ImageUploadToServerFunctionNewGame(){

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
                progressDialog = ProgressDialog.show(Poker_new.this,"Image is Uploading","Please Wait",false,false);

                //progressDialog = new ProgressDialog(Poker_new.this);
                //progressDialog.setMessage("Image is Uploading, Please Wait");
                //progressDialog.setIndeterminate(false);
                //progressDialog.setCancelable(true);
                //progressDialog.show();

                // Showing progress dialog at image upload time.
                //progressDialog = ProgressDialog.show(Poker_new.this),"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                //new UpdatePlayer().execute();
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();


            }

            @Override
            protected String doInBackground(Void... params) {

                Poker_new.ImageProcessClass imageProcessClass = new Poker_new.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
                HashMapParams.put("id", Poker_new.newidjogo);
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
    public void LoadPhotos() {


                if(bitmap != null){

                    ImageUploadToServerFunctionNewGame();
                }


    }
}
