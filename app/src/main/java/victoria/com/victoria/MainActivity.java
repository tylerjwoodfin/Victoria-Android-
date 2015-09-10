package victoria.com.victoria;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends ActionBarActivity
{
    String   name;
    EditText mEdit;
    TextView mText;

    public void toast(String message)
    {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public void saveData(String filename, String contents)
    {
        try
        {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(contents.getBytes());
            fos.close();
            //toast("Saved Successfully under Title: " + filename);
        }
        catch(Exception e)
        {
            toast("There was a problem saving.");
        }
    }

    public int loadInt(String filename)
    {
        try
        {
            FileInputStream fileIn=openFileInput(filename);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0)
            {
                // char to string conversion
                String string=String.copyValueOf(inputBuffer,0,charRead);
                s +=string;
            }
            InputRead.close();
           // toast(filename + " read successfully");
            return Integer.parseInt(s);
        }
        catch(FileNotFoundException e)
        {
            //toast("File Not Found: " + filename);
        }
        catch(IOException e)
        {
            toast("IOException: " + filename);
        }
        return -1;
    }

    public String loadString(String filename)
    {
        try
        {
            FileInputStream fileIn=openFileInput(filename);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0)
            {
                // char to string conversion
                String string=String.copyValueOf(inputBuffer,0,charRead);
                s +=string;
            }
            InputRead.close();
            //toast(filename + " read successfully as " + s);
            return s;
        }
        catch(FileNotFoundException e)
        {
            //toast("File Not Found: " + filename);
            return "FILENOTSET";
        }
        catch(IOException e)
        {
            toast("IOException: " + filename);
        }
        return "ERROR";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mText = (TextView)findViewById(R.id.textView);

        //Process name
        name = loadString("Name");
        if(!name.equals("FILENOTSET"))
            mText.setText("How are you, " + name + "?");

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionID, KeyEvent event)
            {
                boolean handled = false;

                if(actionID > -1)
                {
                    mEdit   = (EditText)findViewById(R.id.editText);
                    String query = mEdit.getText().toString();
                    handled = true;

                    //Name not set
                    if (name.equals("FILENOTSET"))
                    {
                        mText.setText("Hey, "+ query +"! I'm Victoria.");
                        saveData("Name", query);
                    }
                    else
                    if(query.matches("my name is (.*)"))
                        mText.setText("Your new name is: " + query);


                    else
                        mText.setText("You said: " + query);

                    mEdit.setText("");

                } //End Actions after enter

                return handled; //Necessary for override
            }
        });
    }

    public void menuOnClick(MenuItem m)
    {
        String title = (m.getTitle()).toString();
        //Use title for menu title
        if(m.isChecked())
        {
            m.setChecked(false);
            saveData(title, "0");
        }
        else
        {
            m.setChecked(true);
            saveData(title, "1");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Read File
        int DJ = loadInt("DJ");
        if(DJ == 1) {
            int id = R.id.toggle_dj;
            (menu.findItem(id)).setChecked(true);
        }
        int DND = loadInt("Do Not Disturb");
        if(DND == 1)
        {
            int id = R.id.toggle_dnd;
            (menu.findItem(id)).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
