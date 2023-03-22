package com.example.luiseduardo.infrafacil;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class MaskEditUtil {

    public static final String FORMAT_CPF = "###.###.###-##";
    public static final String FORMAT_FONE = "(###)####-#####";
    public static final String FORMAT_CEP = "#####-###";
    public static final String FORMAT_DATE = "##/##/####";
    public static final String FORMAT_HOUR = "##:##";
    public static final String FORMAT_MILHAR1 = "#.###";
    public static final String FORMAT_MILHAR2 = "##.###";

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return
     */
    public static TextWatcher mask(final EditText ediTxt,final String mask) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            String mask;

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = MaskEditUtil.unmask(s.toString());


                switch (str.length()) {
                    case 1:
                        mask = "#";
                        break;
                    case 2:
                        mask = "##";
                        break;
                    case 3:
                        mask = "###";
                        break;
                    case 4:
                        mask = "#.###";
                        break;

                    case 5:
                        mask = "##.###";
                        break;
                    case 6:
                        mask = "###.###";
                        break;
                    case 7:
                        mask = "#.###.###";
                        break;
                    default:
                        mask = "#.###.###";
                        break;
                }

                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int ii = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(ii);
                    } catch (final Exception e) {
                        break;
                    }
                    ii++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());

            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }
}
