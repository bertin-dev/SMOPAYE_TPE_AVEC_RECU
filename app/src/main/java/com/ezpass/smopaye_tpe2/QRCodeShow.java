package com.ezpass.smopaye_tpe2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.Apropos.Apropos;
import com.ezpass.smopaye_tpe2.vuesAccepteur.DebitCarte;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.printer.UsbThermalPrinter;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.ModifierCompte;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class QRCodeShow extends AppCompatActivity {

    private String carte, nom_prenom;
    private ImageView qr_code;
    private TextView card_number;
    private Button btn_enregistrer, btn_imprimer;

    private Bitmap bitmap;
    private String picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.bmp";

    private String Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_show);

        /*getSupportActionBar().setTitle("QR Code");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        qr_code = findViewById(R.id.qrcode);
        card_number = findViewById(R.id.card_number);
        btn_enregistrer = (Button) findViewById(R.id.btn_enregistrer);
        btn_imprimer = (Button) findViewById(R.id.btn_imprimer);


        Intent intent = getIntent();
        carte = intent.getStringExtra("id_carte");
        nom_prenom = intent.getStringExtra("nom_prenom");


        String carteCrypte = "E-ZPASS" + carte.toLowerCase() + ChaineConnexion.getsecurity_keys();
        if(!carteCrypte.isEmpty()){
            try {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(carteCrypte, BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qr_code.setImageBitmap(bitmap);
            } catch (WriterException e){
                e.printStackTrace();
            }
        } else{
            Toast.makeText(this, "Une Erreur est survenue", Toast.LENGTH_SHORT).show();
        }

        card_number.setText(String.valueOf(carteCrypte));


        /*//cryptage du numero de carte
        byte[] bytes = carte.getBytes();
        String mdp = "E-ZPASS by " + getString(R.string.app_name) +  "/" + getPackageName() + "/123456789";
        Toast.makeText(this, mdp, Toast.LENGTH_SHORT).show();
        HashMap<String, byte[]> carteCrypte = encryptBytes(bytes, mdp);

        if(carteCrypte != null && !carteCrypte.isEmpty()){
            try {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(carteCrypte.toString(), BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qr_code.setImageBitmap(bitmap);
            } catch (WriterException e){
                e.printStackTrace();
            }
        } else{
            Toast.makeText(this, "Une Erreur est survenue lors du scan du QR Code", Toast.LENGTH_SHORT).show();
        }
        card_number.setText(String.valueOf(carteCrypte));*/

        btn_enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savepicture();

                startSave();
               /* // Get the bitmap from drawable object
                Bitmap bitmap1 = bitmap;
                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                File file1 = wrapper.getDir("Img",MODE_PRIVATE);

                // Create a file to save the image
                file1 = new File(file1, "qrcode"+".jpg");

                try{
                    OutputStream stream = null;
                    stream = new FileOutputStream(file1);
                    bitmap1.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    stream.flush();
                    stream.close();

                }catch (IOException e) // Catch the exception
                {
                    e.printStackTrace();
                }

                // Parse the gallery image url to uri
                Uri savedImageURI = Uri.parse(file1.getAbsolutePath());

                // Display the saved image to ImageView
                //iv_saved.setImageURI(savedImageURI);

                // Display saved image uri to TextView
                //tv_saved.setText("Image saved in internal storage.\n" + savedImageURI);

                Toast.makeText(wrapper, savedImageURI.toString(), Toast.LENGTH_SHORT).show();*/
            }
        });


        btn_imprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        UsbThermalPrinter usbThermalPrinter = new UsbThermalPrinter(QRCodeShow.this);
                        try {
                            //lOGO DE l'Entreprise
                            usbThermalPrinter.start(1);
                            usbThermalPrinter.reset();
                            usbThermalPrinter.setMonoSpace(true);
                            usbThermalPrinter.setGray(7);
                            usbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE);
                            Bitmap bitmap4 = BitmapFactory.decodeResource(QRCodeShow.this.getResources(), R.drawable.logo_moy);
                            Bitmap bitmap5 = ThumbnailUtils.extractThumbnail(bitmap4, 244, 150);
                            usbThermalPrinter.printLogo(bitmap5, true);

                            usbThermalPrinter.setTextSize(30);
                            usbThermalPrinter.addString("E-ZPASS");
                            usbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_LEFT);
                            usbThermalPrinter.setTextSize(24);

                            //QR CODE
                            //usbThermalPrinter.start(1);
                            Bitmap bitmap = CreateCode(carte, BarcodeFormat.QR_CODE, 384, 384);
                            //usbThermalPrinter.reset();
                            //usbThermalPrinter.setGray(7);
                            usbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE);
                            usbThermalPrinter.printLogo(bitmap,true);

                            //if(!nom_prenom.equals(""))
                            usbThermalPrinter.addString(nom_prenom);


                            //QR Date de génération
                            /*SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            String str = formatter.format(curDate);
                            usbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_RIGHT);
                            usbThermalPrinter.setTextSize(15);
                            usbThermalPrinter.addString(str);*/

                            usbThermalPrinter.printString();//permet d'imprimer le text
                            usbThermalPrinter.walkPaper(10);
                        } catch (TelpoException e) {
                            e.printStackTrace();
                            Result = e.toString();
                            if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                                Toast.makeText(QRCodeShow.this, "NoPaperException", Toast.LENGTH_SHORT).show();
                            } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                                Toast.makeText(QRCodeShow.this, "OverHeatException", Toast.LENGTH_SHORT).show();
                            }
                        } catch (WriterException e) {
                            e.printStackTrace();
                        } finally {
                            usbThermalPrinter.stop();
                        }

                    }
                }).start();
                //Toast.makeText(QRCodeShow.this, "Indisponible", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void startSave() {
        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if(!file.exists() && !file.mkdir()){
            Toast.makeText(this, "Impossible de créer un répertoire pour enregistrer l'image", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "IMG_"+date+".jpg";
        String file_name = file.getAbsolutePath()+"/"+name;
        File new_file=new File(file_name);
        try{
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(qr_code, qr_code.getWidth(), qr_code.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            Toast.makeText(this, "Image Enregistré avec succès.", Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file_name);

        // Display the saved image to ImageView
        //iv_saved.setImageURI(savedImageURI);

        // Display saved image uri to TextView
        //tv_saved.setText("Image saved in internal storage.\n" + savedImageURI);

        Toast.makeText(this, savedImageURI.toString(), Toast.LENGTH_LONG).show();
    }

    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "IMG_Smopaye");
    }

    private void savepicture() {
        File file = new File(picturePath);
        if (!file.exists()) {
            InputStream inputStream = null;
            FileOutputStream fos = null;
            byte[] tmp = new byte[1024];
            try {
                inputStream = getApplicationContext().getAssets().open("syhlogo.png");
                fos = new FileOutputStream(file);
                int length = 0;
                while((length = inputStream.read(tmp)) > 0){
                    fos.write(tmp, 0, length);
                }
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap CreateCode(String str, com.google.zxing.BarcodeFormat type, int bmpWidth, int bmpHeight) throws WriterException {
        Hashtable<EncodeHintType,String> mHashtable = new Hashtable<EncodeHintType,String>();
        mHashtable.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = new MultiFormatWriter().encode(str, type, bmpWidth, bmpHeight, mHashtable);
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    /*                    GESTION DU MENU DROIT                  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.partage){
            //startShare();
            shareBitmap(viewToBitmap(qr_code, qr_code.getWidth(), qr_code.getHeight()));
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.apropos) {
            Intent intent = new Intent(getApplicationContext(), Apropos.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        if (id == R.id.tuto) {
            Intent intent = new Intent(getApplicationContext(), TutorielUtilise.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        if(id == R.id.modifierCompte){
            Intent intent = new Intent(getApplicationContext(), ModifierCompte.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }



    //methode qui fonctionnement uniquement avec les versions inférieur à Android 7.0
    public void startShare() {
        Bitmap bitmap = viewToBitmap(qr_code, qr_code.getWidth(), qr_code.getHeight());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "ImageDemo.jpg");
        try{
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/ImageDemo.jpg"));
        startActivity(Intent.createChooser(shareIntent, "Smopaye Partage QR Code"));
    }


    private static Bitmap viewToBitmap(View view, int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void shareBitmap(Bitmap bitmap) {

        /*final String shareText = " E-ZPASS by" + " "
                + getString(R.string.app_name) + " developed by "
                + "https://play.google.com/store/apps/details?id=" + getPackageName() + ": \n\n";*/


        final String shareText = " E-ZPASS by" + " "
                + getString(R.string.app_name) + ": \n\n";

        try {
            File file = new File(this.getExternalCacheDir(), "share.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "E-ZPASS Partage via"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}