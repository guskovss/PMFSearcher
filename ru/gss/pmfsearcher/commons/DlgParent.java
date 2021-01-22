/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.commons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import ru.gss.pmfsearcher.PMFSearcherApp;

/**
 * Dialog with position in main window and with listener of Esc key.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class DlgParent extends javax.swing.JDialog {

    /**
     * Constructor.
     */
    public DlgParent() {
        super(PMFSearcherApp.getApplication().getMainFrame());
        getRootPane().registerKeyboardAction(new EscActionListenerImpl(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Listener of Esc key.
     */
    private class EscActionListenerImpl implements ActionListener {
        /**
         * Constructor.
         */
        public EscActionListenerImpl() {
        }
        /**
         * Event.
         * @param ae event
         */
        public void actionPerformed(final ActionEvent ae) {
            dispose();
        }
    }
}
