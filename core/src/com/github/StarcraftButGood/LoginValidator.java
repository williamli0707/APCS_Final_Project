package com.github.StarcraftButGood;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * The login widget displayed in the LoginScreen.
 * @author William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class LoginValidator extends VisWindow {
    private Main game;

    /**
     * Constructs the widget.
     * @param width the width of the screen
     * @param height the height of the screen
     * @param game the main class
     */
    public LoginValidator(int width, int height, final Main game) {
        super("Login");

        this.game = game;

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton acceptButton = new VisTextButton("login");
        final VisCheckBox termsCheckbox = new VisCheckBox("create a new account");

        final VisValidatableTextField user = new VisValidatableTextField();
        final VisValidatableTextField pass = new VisValidatableTextField();
//        user.setDebug(true);
//        pass.setDebug(true);
//        cancelButton.setDebug(true);
//        acceptButton.setDebug(true);

        VisLabel errorLabel = new VisLabel();
        errorLabel.setColor(Color.RED);

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(errorLabel).expand().fill();
//        buttonTable.add(cancelButton);
        buttonTable.add(acceptButton);

        add(new VisLabel("Username: "));
        add(user).expand().fill();
        row();
        add(new VisLabel("Password: "));
        add(pass).expand().fill();
        row();
        add(termsCheckbox).colspan(2);
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
                JSONObject res;
                int status;
                try {
                    res = new JSONObject(login(user.getText(), pass.getText(), termsCheckbox.isChecked()));
                    status = res.getInt("status");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (status == 0) {
                    Dialogs.showOKDialog(getStage(), "message", "success!");
                    PlayerData.init(user.getText(), res.getInt("kills"), res.getInt("stars"), res.getInt("games"), res.getInt("wins"));
                    game.menuScreen();
                } else {
                    String expl;
                    //-3 for username already exists, 0 for account created + data follow
                    //-2 for username does not exist, -1 for wrong password
                    switch (status) {
                        case -3:
                            expl = "This username already exists! Try another one. ";
                            break;
                        case -2:
                            expl = "This username does not exist. ";
                            break;
                        case -1:
                            expl = "Incorrect password. Try again. ";
                            break;
                        default:
                            expl = "Unknown error: Error code " + status;
                    }
                    Dialogs.showOKDialog(getStage(), "Login Failed", expl);
                }
            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
        setPosition((width / 2f) - (getWidth() / 2), height / 2f - getHeight() / 2);
    }

    /**
     * Sends a request to a remote server for a login request. Receives and processes the response.
     * @param username the username to sign in with
     * @param password the password to sign in with
     * @param signUp whether the user is signing up (new account)
     * @return the JSON response from the server
     * @throws IOException on response reading failed
     */
    public String login(String username, String password, boolean signUp) throws IOException {
        URL url = new URL("http://192.9.249.213:3000");
//        URL url = new URL("http://localhost:3000");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("stat", String.valueOf(signUp ? 1 : 0));
        parameters.put("username", username);
        parameters.put("password", password);

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();


        int status = con.getResponseCode();

        Reader streamReader;
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
        con.disconnect();
        System.out.println(content);
        return status > 299 ? null : content.toString();
    }

    /**
     * Relocate the widget to be in the center when the window is resized.
     * @param width the new width of the screen
     * @param height the new height of the screen
     */
    public void resize(int width, int height) {
        setPosition((width / 2f) - (getWidth() / 2), height / 2f - getHeight() / 2);
    }
}

/**
 * The helper class to add parameters to the HTTP request.
 * @author William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
class ParameterStringBuilder {
    /**
     * Creates a prameter string from a map.
     * @param params the map of parameters to be included in the HTTP request
     * @return a String of the parameters in correct HTTP format
     * @throws UnsupportedEncodingException possible exception I have no clue what this does
     */
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