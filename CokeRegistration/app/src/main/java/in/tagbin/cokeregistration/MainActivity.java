package in.tagbin.cokeregistration;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

public class MainActivity extends AppCompatActivity {
    EditText name, email, number;
    String nameS, emailS, numberS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        number = (EditText) findViewById(R.id.number);
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameS = name.getText().toString();
                emailS = email.getText().toString();
                numberS = number.getText().toString();
                DatabaseOperations dop = new DatabaseOperations(MainActivity.this);
                dop.putInformation(dop, nameS, emailS, numberS);
                Cursor cursor = dop.getInformation(dop);
                cursor.getColumnCount();
                Log.d("vals", "" + cursor.getColumnName(1));
                ExportDatabaseCSVTask exportDatabaseCSVTask= new ExportDatabaseCSVTask();
                exportDatabaseCSVTask.execute();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean>

    {

        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override

        protected void onPreExecute()

        {

            this.dialog.setMessage("Exporting database...");

            this.dialog.show();

        }


        protected Boolean doInBackground(final String... args)

        {


            File dbFile = getDatabasePath(TableData.Tableinfo.DATABASE_NAME);
            //AABDatabaseManager dbhelper = new AABDatabaseManager(getApplicationContext());
            DatabaseOperations dbhelper = new DatabaseOperations(MainActivity.this);
            System.out.println(dbFile);  // displays the data base path in your logcat


            File exportDir = new File(Environment.getExternalStorageDirectory(), "");

            if (!exportDir.exists())

            {
                exportDir.mkdirs();
            }


            File file = new File(exportDir, "coke.csv");


            try

            {

                if (file.createNewFile()) {
                    System.out.println("File is created!");
                    System.out.println("myfile.csv " + file.getAbsolutePath());
                } else {
                    System.out.println("File already exists.");
                }

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//                SQLiteDatabase db = dbhelper.getWritableDatabase();

//                Cursor curCSV = db.getdb().rawQuery("select * from " + db.TABLE_NAME, null);

                Cursor curCSV= dbhelper.getInformation(dbhelper);
                csvWrite.writeNext(curCSV.getColumnNames());

                while (curCSV.moveToNext())

                {

                    String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2)};

         /*curCSV.getString(3),curCSV.getString(4)};*/

                    csvWrite.writeNext(arrStr);


                }

                csvWrite.close();
                curCSV.close();
        /*String data="";
        data=readSavedData();
        data= data.replace(",", ";");
        writeData(data);*/

                return true;

            } catch (IOException e)

            {

                Log.e("MainActivity", e.getMessage(), e);

                return false;

            }


        }

        protected void onPostExecute(final Boolean success)

        {

            if (this.dialog.isShowing())

            {

                this.dialog.dismiss();

            }

            if (success)

            {
                CSVToExcelConverter csvToExcelConverter= new CSVToExcelConverter();
                csvToExcelConverter.execute();

                Toast.makeText(MainActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();

            }

            else

            {

                Toast.makeText(MainActivity.this, "Export failed", Toast.LENGTH_SHORT).show();

            }
        }}

    public class CSVToExcelConverter extends AsyncTask<String, Void, Boolean> {


        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute()
        {this.dialog.setMessage("Exporting to excel...");
            this.dialog.show();}

        @Override
        protected Boolean doInBackground(String... params) {
            ArrayList arList=null;
            ArrayList al=null;
            String outFilePath;

            //File dbFile= new File(getDatabasePath("database_name").toString());
            File dbFile=getDatabasePath(TableData.Tableinfo.DATABASE_NAME);
            String yes= dbFile.getAbsolutePath();

            String inFilePath = Environment.getExternalStorageDirectory().toString()+"/coke.csv";
             outFilePath = Environment.getExternalStorageDirectory().toString() + "/registrationData.xls";
            String thisLine;
            int count=0;

            try {

                FileInputStream fis = new FileInputStream(inFilePath);
                DataInputStream myInput = new DataInputStream(fis);
                int i=0;
                arList = new ArrayList();
                while ((thisLine = myInput.readLine()) != null)
                {
                    al = new ArrayList();
                    String strar[] = thisLine.split(",");
                    for(int j=0;j<strar.length;j++)
                    {
                        al.add(strar[j]);
                    }
                    arList.add(al);
                    System.out.println();
                    i++;
                }} catch (Exception e) {
                System.out.println("shit");
            }

            try
            {
                HSSFWorkbook hwb = new HSSFWorkbook();
                HSSFSheet sheet = hwb.createSheet("new sheet");
                for(int k=0;k<arList.size();k++)
                {
                    ArrayList ardata = (ArrayList)arList.get(k);
                    HSSFRow row = sheet.createRow((short) 0+k);
                    for(int p=0;p<ardata.size();p++)
                    {
                        HSSFCell cell = row.createCell((short) p);
                        String data = ardata.get(p).toString();
                        if(data.startsWith("=")){
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            data=data.replaceAll("\"", "");
                            data=data.replaceAll("=", "");
                            cell.setCellValue(data);
                        }else if(data.startsWith("\"")){
                            data=data.replaceAll("\"", "");
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(data);
                        }else{
                            data=data.replaceAll("\"", "");
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(data);
                        }
                        //*/
                        // cell.setCellValue(ardata.get(p).toString());
                    }
                    System.out.println();
                }
                FileOutputStream fileOut = new FileOutputStream(outFilePath);
                hwb.write(fileOut);
                fileOut.close();
                System.out.println("Your excel file has been generated");
            } catch ( Exception ex ) {
                ex.printStackTrace();
            } //main method ends
            return true;
        }

        protected void onPostExecute(final Boolean success)

        {

            if (this.dialog.isShowing())

            {

                this.dialog.dismiss();

            }

            if (success)

            {

                Toast.makeText(MainActivity.this, "file is built!", Toast.LENGTH_LONG).show();

            }

            else

            {

                Toast.makeText(MainActivity.this, "file fail to build", Toast.LENGTH_SHORT).show();

            }

        }
    }
}