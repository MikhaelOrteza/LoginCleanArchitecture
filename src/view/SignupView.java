package view;

import interface_adapter.SignupController;
import interface_adapter.SignupState;
import interface_adapter.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {
    public final String viewName = "sign up";
    private final SignupViewModel signupViewModel; // Stores the state of the sign up state.
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final SignupController signupController;

    private final JButton signUp;
    private final JButton cancel;

    public SignupView(SignupController controller, SignupViewModel signupViewModel) {

        this.signupController = controller;
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(signupViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(signupViewModel.USERNAME_LABEL), usernameInputField);
        LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(signupViewModel.PASSWORD_LABEL), passwordInputField);
        LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(signupViewModel.REPEAT_PASSWORD_LABEL), repeatPasswordInputField);

        JPanel buttons = new JPanel();
        signUp = new JButton(signupViewModel.SIGNUP_BUTTON_LABEL);
        buttons.add(signUp);
        cancel = new JButton(signupViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(cancel);

        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource().equals(signUp)){
                    /* ORIGINAL VERSION
                    signupController.execute(usernameInputField.getText(),
                            String.valueOf(passwordInputField.getPassword()),
                            String.valueOf(repeatPasswordInputField.getPassword()));
                     */
                    //Rewrite the code here:
                    signupController.execute(signupViewModel.getState().getUsername(),
                            signupViewModel.getState().getPassword(), signupViewModel.getState().getRepeatPassword());
                }
            }
        });
        cancel.addActionListener(this);

        // This makes a new KeyListener object and makes it listen to keystrokes
        // in the usernameInputField. We're actually creating an anonymous class
        // here that is a subclass of KeyListener. Notice how it has access to
        // instance variables in the enclosing class.
        usernameInputField.addKeyListener(new KeyListener() {
            //NOTES: "new KeyListener()" is an anonymous subclass of Key Listener.
            //When you initliaze a key, this method specifies how you should react to a key stroke.
            @Override
            public void keyTyped(KeyEvent e) {
                SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        passwordInputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { //Updates the shown password in the sign up view model.
                SignupState currentState = signupViewModel.getState();
                currentState.setPassword(passwordInputField.getText());
                signupViewModel.setState(currentState);
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        repeatPasswordInputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(repeatPasswordInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(usernameInfo);
        this.add(passwordInfo);
        this.add(repeatPasswordInfo);
        this.add(buttons);

    }

    /**
     * React to a button click that results in evt.
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(signUp)) {
            signupController.execute(usernameInputField.getText(),
                    String.valueOf(passwordInputField.getPassword()),
                    String.valueOf(repeatPasswordInputField.getPassword()));
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SignupState state = (SignupState) evt.getNewValue();
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }
}