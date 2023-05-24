package com.github;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginValidator extends VisWindow {

    private VisTable buttonTable;

    public LoginValidator (int width, int height) {
        super("Login");

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton cancelButton = new VisTextButton("cancel");
        VisTextButton acceptButton = new VisTextButton("accept");

        final VisValidatableTextField user = new VisValidatableTextField();
        final VisValidatableTextField pass = new VisValidatableTextField();

        VisLabel errorLabel = new VisLabel();
        errorLabel.setColor(Color.RED);

        buttonTable = new VisTable(true);
        buttonTable.add(errorLabel).expand().fill();
//        buttonTable.add(cancelButton);
        buttonTable.add(acceptButton);

        add(new VisLabel("Username: "));
        add(user).expand().fill();
        row();
        add(new VisLabel("Password: "));
        add(pass).expand().fill();
        row();
        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        SimpleFormValidator validator; //for GWT compatibility
        validator = new SimpleFormValidator(acceptButton, errorLabel, "smooth");
//        validator.setSuccessMessage("all good!");
        validator.notEmpty(user, "Username cannot be empty");
        validator.notEmpty(pass, "Password cannot be empty");

        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int res;
                try {
                    res = login(user.getText(), pass.getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(res == 1) {
                    Dialogs.showOKDialog(getStage(), "message", "success!");
                }
                else {
                    Dialogs.showOKDialog(getStage(), "message", "failed!");
                }
            }
        });
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialogs.showOKDialog(getStage(), "message", "you can't escape this!");
            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
        setPosition((width / 2f) - (getWidth() / 2), height / 2f - getHeight() / 2);
    }

    public int login(String username, String password) throws IOException {
        URL url = new URL("http://192.9.249.213:3000");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();

        con.setRequestProperty("Content-Type", "application/json");

        int status = con.getResponseCode();

        Reader streamReader = null;
        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }
        BufferedReader in = new BufferedReader(streamReader);

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        System.out.println(content);
        con.disconnect();

        return -1;
    }

    public void resize(int width, int height) {
//        setPosition((width / 2f) - (getWidth() / 2), height / 2f - getHeight() / 2);
    }
}

class ParameterStringBuilder {
    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}