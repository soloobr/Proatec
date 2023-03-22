package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.MoneyTextWatcher.getCurrencySymbol;
import static com.example.luiseduardo.infrafacil.Poker.lsplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class customAdapter extends RecyclerView.Adapter<customAdapter.ViewHolder> implements MyDialogFragment.DialogListener {

    private  List<PlayersListView> list;
    private int rowLayout;
    private Context mContext;
    private ItemClickListener clickListener;
    public static String  sUsername,idplayer,idjogo,rebuy, addon, valor, vlrebuy,vladdon,vlentrada,totalrebuy,totaladdon,totalplayers,imageuser;

    private static String total = "0";
    JSONParser jsonParser = new JSONParser();
    private static String URLDELETE = "http://futsexta.16mb.com/Poker/Poker_Delete_Players.php";
    private static String URLUP = "http://futsexta.16mb.com/Poker/Poker_Edit_Players.php";
    private static String URTOTAIS = "http://futsexta.16mb.com/Poker/Poker_Get_Totais.php";
    private static String urladdplayers = "http://futsexta.16mb.com/Poker/Poker_insert_Players_Jogo.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private final Locale locale = Locale.getDefault();
    JSONObject object =null;
    private  int Pozi;
    static Boolean Delete = false;
    static Boolean Edit = false;
    private static RecyclerView recyclerView;
    private static customAdapter  mAdapter;
    private ProgressDialog pDialog;
    private boolean PLAYERS = false;

    //private Uri fileUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;

    TextView textView;
    MyDialogFragment dialogFragment;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    private static View layout;
    private WeakReference<CircleImageView> imageViewReference;


    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    private Context context;

    public static String URL = "Paste your URL here";

    JSONParser jsonParserR = new JSONParser();
    private static String urlplayers = "http://futsexta.16mb.com/Poker/poker_get_playersjogo.php";

    public customAdapter( List<PlayersListView> list,int rowLayout,Context context) {
        this.list = list;
        this.rowLayout = rowLayout;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder myViewHolder, int position) {
        final PlayersListView city = list.get(position);

        myViewHolder.title.setText( city.getNome());
        //myViewHolder.title.setText( list.get(position));
        myViewHolder.tv_idplayer.setText(city.getId());
        myViewHolder.tv_nome.setText(city.getNome());
        myViewHolder.tv_qtdrebuy.setText(city.getRebuy());
        myViewHolder.tv_qtdaddon.setText(city.getAddon());
        myViewHolder.tv_imgpatch = city.getImgpatch();
        myViewHolder.tv_rebuy = city.getRebuy();
        myViewHolder.tv_addon = city.getAddon();

        idjogo = city.getIdjogo();
        vlentrada = city.getVlentrada();
        vlrebuy = city.getVlrebuy();
        vladdon = city.getVladdon();
        valor = city.getValor();
        sUsername = city.getNome();
        imageuser = city.getImgpatch();

        if(imageuser.equals("0")){
            Picasso.with(mContext).load("http://futsexta.16mb.com/Poker/imgplayer/useredit.png").into(myViewHolder.tv_imglist);
        }else{
            if(Poker.reload){
                Picasso.with(mContext).load(imageuser).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(myViewHolder.tv_imglist);

            }else{
                Picasso.with(mContext).load(imageuser).into(myViewHolder.tv_imglist);
            }
        }
        //Picasso.with(mContext).load(imageuser).into(myViewHolder.tv_imglist);
        //rebuy = city.getRebuy();
        //addon = city.getAddon();

        //int ent = (int)Double.parseDouble(vlentrada);
        //int valorp = (int)Double.parseDouble(valor);
        //int vlt = ent + valorp;


        BigDecimal parsed = parseToBigDecimal(valor);
        String formatted;
        formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
        myViewHolder.tv_valortotal.setText(formatted);


    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
        //return list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        if (TextUtils.isEmpty(inputText)) {
            textView.setText("Email was not entered");
        } else
            textView.setText("Email entered: " + inputText);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public ImageButton btnaddon;
        public ImageButton btnrebuy;
        private TextView tv_idplayer;
        private TextView tv_nome;
        private TextView tv_qtdrebuy;
        private TextView tv_qtdaddon;
        private TextView tv_valortotal;
        private String tv_imgpatch;
        private String tv_rebuy;
        private String tv_addon;
        private CircleImageView tv_imglist;

        //private Button btn_addplayers;
        //private String tv_vlrebuy;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.main_line_nome);
            //btnaddon = (ImageButton) view.findViewById(R.id.bntaddon);
            //btnrebuy = (ImageButton) view.findViewById(R.id.btnrebuy);

            tv_idplayer = (TextView) itemView.findViewById(R.id.idplayer);
            tv_nome = (TextView) itemView.findViewById(R.id.main_line_nome);
            tv_qtdrebuy = (TextView)itemView.findViewById(R.id.main_line_valorrebuy);
            tv_qtdaddon = (TextView)itemView.findViewById(R.id.main_line_valoraddon);
            tv_valortotal = (TextView)itemView.findViewById(R.id.main_line_valortotal);
            tv_imglist = (CircleImageView)itemView.findViewById(R.id.imgaddplayerslist);
            //tv_imgpatch =
        //btn_addplayers = (Button)itemView.findViewById(R.id.btnewplayers);

            view.setTag(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mContext != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.custom_alertplayers,null);
                        TextView tvcancel = (TextView)view.findViewById(R.id.btn_cancel);
                        TextView tvdisconnect = (TextView)view.findViewById(R.id.btn_confirmar);

                        builder.setView(view);
                        final AlertDialog ad = builder.show();
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                        idplayer = String.valueOf(tv_idplayer.getText());
                        //rebuy = String.valueOf(tv_qtdrebuy.getText());
                        //addon = String.valueOf(tv_qtdaddon.getText());
                        //valor = String.valueOf(tv_valortotal);
                        imageuser = tv_imgpatch;

                        final CircleImageView imglist = (CircleImageView) view.findViewById(R.id.imgaddplayerslist);

                        if(imageuser.equals("0")){
                            Picasso.with(mContext).load("http://futsexta.16mb.com/Poker/imgplayer/useredit.png").into(imglist);
                        }else{
                            if(Poker.reload){
                                Picasso.with(mContext).load(imageuser).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(imglist);

                            }else{
                                Picasso.with(mContext).load(imageuser).into(imglist);
                            }
                        }

                        final TextView tvqtdaddon = view.findViewById(R.id.alqtdaddon);
                        tvqtdaddon.setText(String.valueOf(tv_qtdaddon.getText()));

                        final TextView tvvalor = view.findViewById(R.id.tvvalor);
                        tvvalor.setText(String.valueOf(tv_valortotal.getText()));

                        final TextView tvnome = view.findViewById(R.id.alnomeplayer);
                        tvnome.setText(String.valueOf(tv_nome.getText()));

                        final TextView tvid = view.findViewById(R.id.alidplayer);
                        tvid.setText(String.valueOf(tv_idplayer.getText()));

                        final TextView tvqtdreb = view.findViewById(R.id.alqtdrebuy);
                        tvqtdreb.setText(String.valueOf(tv_qtdrebuy.getText()));

                        final CheckBox tvaddontrue = view.findViewById(R.id.aladdontrue);
                        tvaddontrue.setText(String.valueOf(tv_qtdaddon.getText()));
                        if (tv_qtdaddon.getText().equals("0")) {
                            tvaddontrue.setText(String.valueOf("Não"));
                            tvaddontrue.setChecked(false);


                        }
                        if (tv_qtdaddon.getText().equals("1")) {
                            tvaddontrue.setText(String.valueOf("Sim"));
                            tvaddontrue.setChecked(true);


                        }


                        //alert.setView(promptView);
                        //alert.setCancelable(false);

                        ImageButton btn_1 = (ImageButton) view.findViewById(R.id.btndeleterebuy);
                        btn_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                //do required function
                                // don't forget to call alertD.dismiss()
                                //Toast.makeText(mContext, "DELETE Rebuy", Toast.LENGTH_SHORT).show();
                                addon = String.valueOf(tvqtdaddon.getText());

                                if (tvqtdreb.getText().equals("0")) {
                                    Toast.makeText(mContext, "Rebuy não pode ser negativo!", Toast.LENGTH_SHORT).show();
                                } else {
                                    rebuy = String.valueOf(tvqtdreb.getText());

                                    int reb = (int) Double.parseDouble(rebuy);

                                    reb = reb - 1;
                                    tvqtdreb.setText(String.valueOf(reb));
                                }

                                int ent = (int) Double.parseDouble(vlentrada);
                                int vlreb = (int) Double.parseDouble(vlrebuy);
                                int vlad = (int) Double.parseDouble(vladdon);

                                int reb = (int) Double.parseDouble(String.valueOf(tvqtdreb.getText()));
                                int rebparcial = reb * vlreb;

                                int ads = (int) Double.parseDouble(addon);
                                int adparcial = ads * vlad;

                                int total = rebparcial + adparcial + ent;


                                String valorr = String.valueOf(total);

                                BigDecimal parsed = parseToBigDecimal(valorr);
                                String formatted;
                                formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
                                tvvalor.setText(formatted);

                            }
                        });
                        ImageButton btn_2 = (ImageButton) view.findViewById(R.id.btnaddrebuy);
                        btn_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //do required function
                                // don't forget to call alertD.dismiss()
                                //Toast.makeText(mContext, "ADD Rebuy", Toast.LENGTH_SHORT).show();
                                rebuy = String.valueOf(tvqtdreb.getText());
                                addon = String.valueOf(tvqtdaddon.getText());

                                int reb = (int) Double.parseDouble(rebuy);

                                reb = reb + 1;
                                tvqtdreb.setText(String.valueOf(reb));
                                //tv_qtdrebuy.setText(String.valueOf(reb));

                                int ent = (int) Double.parseDouble(vlentrada);
                                int vlreb = (int) Double.parseDouble(vlrebuy);
                                int vlad = (int) Double.parseDouble(vladdon);

                                int reb1 = (int) Double.parseDouble(String.valueOf(tvqtdreb.getText()));
                                int rebparcial = reb1 * vlreb;

                                int ads = (int) Double.parseDouble(addon);
                                int adparcial = ads * vlad;

                                int total = rebparcial + adparcial + ent;


                                String valorr = String.valueOf(total);

                                BigDecimal parsed = parseToBigDecimal(valorr);
                                String formatted;
                                formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
                                tvvalor.setText(formatted);
                            }
                        });

                        //alert.setCancelable(true);

                        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);


                        Button btn_confirmar = (Button) view.findViewById(R.id.btn_confirmar);
                        btn_confirmar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                idplayer = String.valueOf(tv_idplayer.getText());
                                rebuy = String.valueOf(tvqtdreb.getText());
                                addon = String.valueOf(tvqtdaddon.getText());
                                sUsername = String.valueOf(tv_nome.getText());


                                int ent = (int) Double.parseDouble(vlentrada);
                                int vlreb = (int) Double.parseDouble(vlrebuy);
                                int vlad = (int) Double.parseDouble(vladdon);

                                int reb = (int) Double.parseDouble(rebuy);
                                int rebparcial = reb * vlreb;

                                int ads = (int) Double.parseDouble(addon);
                                int adparcial = ads * vlad;

                                int total = rebparcial + adparcial + ent;


                                valor = String.valueOf(total);

                                //String valor = mData.get(position).getValoruni();
                                BigDecimal parsed = parseToBigDecimal(valor);
                                String formatted;
                                formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
                                //holder.tv_valor.setText(formatted);
                                //tv_valortotal.setText(formatted);
                                tv_qtdrebuy.setText(String.valueOf(reb));
                                tv_qtdaddon.setText(addon);
                                new UpdatePlayer().execute();
                               ad.dismiss();
                                //Poker.vltotaljogo.setText(formatted);
                            }


                        });

                        //CheckBox btn_1= (ImageButton) promptView.findViewById(R.id.btndeleterebuy);
                        tvaddontrue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //addon = String.valueOf(tvqtdaddon.getText());
                                if (tvaddontrue.getText() == "Sim") {

                                    tvaddontrue.setText(String.valueOf("Não"));
                                    tvaddontrue.setChecked(false);
                                    tvqtdaddon.setText("0");

                                    rebuy = String.valueOf(tvqtdreb.getText());
                                    addon = String.valueOf(tvqtdaddon.getText());

                                    int ent = (int) Double.parseDouble(vlentrada);
                                    int vlreb = (int) Double.parseDouble(vlrebuy);
                                    int vlad = (int) Double.parseDouble(vladdon);

                                    int reb = (int) Double.parseDouble(String.valueOf(tvqtdreb.getText()));
                                    int rebparcial = reb * vlreb;

                                    int ads = (int) Double.parseDouble(addon);
                                    int adparcial = ads * vlad;

                                    int total = rebparcial + adparcial + ent;


                                    String valorr = String.valueOf(total);

                                    BigDecimal parsed = parseToBigDecimal(valorr);
                                    String formatted;
                                    formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
                                    tvvalor.setText(formatted);
                                } else if (tvaddontrue.getText() == "Não") {
                                    tvaddontrue.setText(String.valueOf("Sim"));
                                    tvaddontrue.setChecked(true);
                                    tvqtdaddon.setText("1");

                                    rebuy = String.valueOf(tvqtdreb.getText());
                                    addon = String.valueOf(tvqtdaddon.getText());

                                    int ent = (int) Double.parseDouble(vlentrada);
                                    int vlreb = (int) Double.parseDouble(vlrebuy);
                                    int vlad = (int) Double.parseDouble(vladdon);

                                    int reb = (int) Double.parseDouble(String.valueOf(tvqtdreb.getText()));
                                    int rebparcial = reb * vlreb;

                                    int ads = (int) Double.parseDouble(addon);
                                    int adparcial = ads * vlad;

                                    int total = rebparcial + adparcial + ent;


                                    String valorr = String.valueOf(total);

                                    BigDecimal parsed = parseToBigDecimal(valorr);
                                    String formatted;
                                    formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
                                    tvvalor.setText(formatted);
                                }


                            }
                        });



                        //final AlertDialog dialog = alert.create();
                        //final AlertDialog dialog = alert.show();
                        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        //dialog.show();

                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mContext, "Movimentação cancelada", Toast.LENGTH_SHORT).show();
                                ad.dismiss();
                            }
                        });




                       /* tvdisconnect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.dismiss();

                            }
                        });*/
/*
                        tvcancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.dismiss();
                            }
                        });*/
                        /*
                        int position = getAdapterPosition();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                        View promptView = inflater.inflate(R.layout.custom_alertplayers, null);
                        //View promptView = inflater.inflate(R.layout.custom_alertplayers, null);

                        //LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                        builder.setView(promptView);
                        final AlertDialog ad = builder.show();*/



                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    idplayer = String.valueOf(tv_idplayer.getText());
                    //Toast.makeText(mContext, "Long! " + idplayer, Toast.LENGTH_SHORT).show();
                    sUsername = String.valueOf(tv_nome.getText());
                    Pozi = getAdapterPosition();
                    imageuser = tv_imgpatch;
                    rebuy = tv_rebuy;
                    addon = tv_addon;
                    valor = String.valueOf(tv_valortotal);

                    //idplayer = String.valueOf(tv_idplayer.getText());
                    //rebuy = String.valueOf(tvqtdreb.getText());
                    //addon = String.valueOf(tvqtdaddon.getText());
                    //sUsername = String.valueOf(tv_nome.getText());


                    int ent = (int) Double.parseDouble(vlentrada);
                    int vlreb = (int) Double.parseDouble(vlrebuy);
                    int vlad = (int) Double.parseDouble(vladdon);

                    int reb = (int) Double.parseDouble(rebuy);
                    int rebparcial = reb * vlreb;

                    int ad = (int) Double.parseDouble(addon);
                    int adparcial = ad * vlad;

                    int total = rebparcial + adparcial + ent;


                    valor = String.valueOf(total);


                    myPopupMenu(view);

                    return true;
                }

            });
       }

        @Override
        public void onClick(View view) {

        }
    }
    private void myPopupMenu(View v) {

        PopupMenu popupMenu = new PopupMenu(mContext, v);

        /*  The below code in try catch is responsible to display icons*/
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast. Maketext (mainactivity. This, "clicked" + item. Gettitle(), toast. Length_short). Show();

                switch (item.getItemId()) {
                    case R.id.delete:
                        //Toast.makeText(mContext, "clicked delete" + idplayer + " "+idjogo, Toast.LENGTH_SHORT).show();
                        showAlert(mContext, "Deletar", "Deletando Player");
           //new DeletePlayer().execute();
                         return true;
                    case R.id.edite:
                        FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                        MyDialogFragment df = new MyDialogFragment();
                        df.show(ft,"dialog");
                        //Toast.makeText(mContext, "clicked edite" + idplayer, Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return true;
                }
            }
        });
        //Show menu
        popupMenu.show();
    }
    public class DeletePlayer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {

           int success;
            try {

                List params = new ArrayList();

                params.add(new BasicNameValuePair("id", idplayer));
                params.add(new BasicNameValuePair("idjogo", idjogo));

                JSONObject newjson = jsonParser.makeHttpRequest(URLDELETE, "POST",
                        params);

                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Deletado com successo!", newjson.toString());
                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Não Deletado", newjson.getString(TAG_MESSAGE));
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            new GetTotais().execute();
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

                    params.add(new BasicNameValuePair("id", idplayer));
                    params.add(new BasicNameValuePair("rebuy", rebuy));
                    params.add(new BasicNameValuePair("addon", addon));
                    params.add(new BasicNameValuePair("valor", valor));
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
            //new Poker.GetDados().execute();
            new GetTotais().execute();
            new GetDados().execute();
        }

    }
    class GetTotais extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String  doInBackground(String... args) {


            List params = new ArrayList();
            params.add(new BasicNameValuePair("id",idjogo));

            JSONObject json = jsonParser.makeHttpRequest(URTOTAIS,"POST",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("jogo");

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
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


            int vl = (int)Double.parseDouble(total);

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

    public void showAlert(Context MMcontext, String title, String message){


        AlertDialog.Builder alert = new AlertDialog.Builder(MMcontext);
        LayoutInflater layoutInflater = LayoutInflater.from(MMcontext);
        View promptView = layoutInflater.inflate(R.layout.custom_alertnewplayer, null);



        if (title.equals("Deletar")){
            final ImageView img = promptView.findViewById(R.id.imgaddplayers);
            //img.setImageResource(R.mipmap.usercircledelete);
            if(imageuser.equals("0")){
                Picasso.with(mContext).load("http://futsexta.16mb.com/Poker/imgplayer/userdelete.png").into(img);
            }else{
                Picasso.with(mContext).load(imageuser).into(img);
            }

            final TextView nameview = promptView.findViewById(R.id.tvname);
            nameview.setText(sUsername);

            final TextView tvaction = promptView.findViewById(R.id.tvactionplayer);
            tvaction.setVisibility(View.VISIBLE);



            final TextView txnome = promptView.findViewById(R.id.txnomep);
            txnome.setVisibility(View.GONE);
            final EditText ednome = promptView.findViewById(R.id.ednomePlayer);
            //ednome.setText(sUsername);
            //ednome.setEnabled(false);
            //ednome.setFocusable(false);
            ednome.setVisibility(View.GONE);
            Delete = true;
        }

        if (title.equals("Editar")){

            CircleImageView img = promptView.findViewById(R.id.imgaddplayers);
            //img.setImageResource(R.mipmap.usercirclegear128);
            //img.setImageResource(R.mipmap.boxout128);
            //Picasso.with(mContext).load("https://lh3.googleusercontent.com/-RYaeIsr3gxQ/AAAAAAAAAAI/AAAAAAAAAAA/AMZuuclMBGUWtlvkOi5n-9B_ltzdNbJzvQ/photo.jpg?sz=46").into(img);

            if(imageuser.equals("0")){
                Picasso.with(mContext).load("http://futsexta.16mb.com/Poker/imgplayer/useredit.png").into(img);
            }else{
                Picasso.with(mContext).load(imageuser).into(img);
            }
            layout = View.inflate(mContext, R.layout.custom_alertnewplayer, null);
            CircleImageView imgg = promptView.findViewById(R.id.imgaddplayers);




            //img.setBackground(ContextCompat.getDrawable(context,R.drawable.img));
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ContextCompat.checkSelfPermission(MMcontext,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions((Activity) MMcontext, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        Toast.makeText(mContext, "sem permissão", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        ((Activity)MMcontext).startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }


                    //Toast.makeText(mContext, "FOTO", Toast.LENGTH_SHORT).show();
                    //if (context.getApplicationContext().getPackageManager().hasSystemFeature(
                    //        PackageManager.FEATURE_CAMERA)) {
                        /*
                        // Open default camera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Poker.fileUri);

                        // start the image capture Intent ((Activity)context)
                        ((Activity)context).startActivityForResult(intent, 100);*/

                   // } else {
                   //     Toast.makeText(mContext, "Camera not supported", Toast.LENGTH_LONG).show();
                   // }
                }


            });



            final TextView tvaction = promptView.findViewById(R.id.tvactionplayer);
            tvaction.setText("Editando Player");



            final EditText ednome = promptView.findViewById(R.id.ednomePlayer);
            ednome.setText(sUsername);
            ednome.setSelection(ednome.getText().length());
            ednome.setEnabled(true);

            Edit = true;
        }

        alert.setView(promptView);
        alert.setCancelable(false);
        //Poker.this.setFinishOnTouchOutside(false);

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Delete = false;
                Edit = false;
                //Toast.makeText(context, "Edição cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Delete){

                    new DeletePlayer().execute();
                    list.remove(Pozi);
                    notifyItemRemoved(Pozi);
                    Delete = false;
                    //TextView noplayers = (TextView) findViewById(R.id.tvsemplayers);
                    if (list.isEmpty()){
                        Poker.noplayers.setVisibility(View.VISIBLE);
                    }

                }
                if (Edit){
                    final EditText ednome = promptView.findViewById(R.id.ednomePlayer);
                    sUsername = String.valueOf(ednome.getText());

                    new UpdatePlayer().execute();

                    ((InputMethodManager) MMcontext.getSystemService(MMcontext.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ednome.getWindowToken(), 0);
                    Edit = false;
                }


            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();




    }

    public class GetDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Atualizando Dados...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            int success = 0;
            List params = new ArrayList();
            params.add(new BasicNameValuePair("id",idjogo));

            JSONObject jsonStrT = jsonParserR.makeHttpRequest(urlplayers,"POST",
                    params);

            try {
                success = jsonStrT.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (success == 1) {
                try {
                    JSONObject jsonObj = new JSONObject(String.valueOf(jsonStrT));

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("jogo");

                    lsplayer = new ArrayList<PlayersListView>();

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String idjogo1 = c.getString("idjogo");
                        String id = c.getString("id");
                        String name = c.getString("Nome_Player");
                        String rebuy = c.getString("rebuy");
                        String addon = c.getString("addon");
                        String valor = c.getString("Valor");
                        String vlentrada = c.getString("vlentrada");
                        String vlrebuy = c.getString("vlrebuy");
                        String vladdon = c.getString("vladdon");
                        String img = c.getString("image_path");


                        if (valor != null) {
                            PLAYERS = true;
                            Poker.lsplayer.add(new PlayersListView(id, idjogo1, name,  rebuy,  addon, valor,vlentrada,vlrebuy,vladdon,1,img));
                        }

                    }

                } catch (final JSONException e) {

                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog


            //tx.setText(contador);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (PLAYERS) {
                int spanCount = 2;
                //Poker.recyclerView = (RecyclerView) findViewById(R.id.listviwerplayers);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
                //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                Poker.recyclerView.setLayoutManager(gridLayoutManager);
                Poker.recyclerView.setItemAnimator(new DefaultItemAnimator());
                Poker.mAdapter = new customAdapter(lsplayer, R.layout.item_players, mContext);
                Poker.recyclerView.setAdapter(Poker.mAdapter);
                //mAdapter.setClickListener(mContext);
            }else{
                Poker.noplayers.setVisibility(View.VISIBLE);
            }


        }
    }


    /*public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }*/
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }*/

}