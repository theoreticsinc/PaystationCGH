/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInteface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author Theoretics
 */
public class LookAheadTextField extends JTextField {
  public LookAheadTextField() {
    this(0, null);
  }

  public LookAheadTextField(int columns) {
    this(columns, null);
  }

  public LookAheadTextField(int columns, TextLookAhead lookAhead) {
    super(columns);
    setLookAhead(lookAhead);
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        // Remove any existing selection
        setCaretPosition(getDocument().getLength());
      }
    });
    addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent evt) {
      }

      public void focusLost(FocusEvent evt) {
        if (evt.isTemporary() == false) {
          // Remove any existing selection
          setCaretPosition(getDocument().getLength());
        }
      }
    });
  }

  public void setLookAhead(TextLookAhead lookAhead) {
    this.lookAhead = lookAhead;
  }

  public TextLookAhead getLookAhead() {
    return lookAhead;
  }

  public void replaceSelection(String content) {
    super.replaceSelection(content);

    if (isEditable() == false || isEnabled() == false) {
      return;
    }

    Document doc = getDocument();
    if (doc != null && lookAhead != null) {
      try {
        String oldContent = doc.getText(0, doc.getLength());
        String newContent = lookAhead.doLookAhead(oldContent);
        if (newContent != null) {
          // Substitute the new content
          setText(newContent);

          // Highlight the added text
          setCaretPosition(newContent.length());
          moveCaretPosition(oldContent.length());
        }
      } catch (BadLocationException e) {
        // Won't happen
      }
    }
  }

  protected TextLookAhead lookAhead;

  // The TextLookAhead interface
  public interface TextLookAhead {
    public String doLookAhead(String key);
  }
}

class StringArrayLookAhead implements LookAheadTextField.TextLookAhead {
  public StringArrayLookAhead() {
    values = new String[0];
  }

  public StringArrayLookAhead(String[] values) {
    this.values = values;
  }

  public void setValues(String[] values) {
    this.values = values;
  }

  public String[] getValues() {
    return values;
  }

  public String doLookAhead(String key) {
    int length = values.length;

    // Look for a string that starts with the key
    for (int i = 0; i < length; i++) {
      if (values[i].startsWith(key) == true) {
        return values[i];
      }
    }

    // No match found - return null
    return null;
  }

  protected String[] values;
}
