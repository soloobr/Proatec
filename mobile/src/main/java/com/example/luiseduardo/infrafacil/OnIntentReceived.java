package com.example.luiseduardo.infrafacil;

import android.content.Intent;

public interface OnIntentReceived {

    void onIntent(Intent i, int resultCode);

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
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
