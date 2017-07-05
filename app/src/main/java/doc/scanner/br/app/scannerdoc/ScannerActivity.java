package doc.scanner.br.app.scannerdoc;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import doc.scanner.br.app.scannerdoc.doc.scanner.br.app.scanner.android.IntentIntegrator;
import doc.scanner.br.app.scannerdoc.doc.scanner.br.app.scanner.android.IntentResult;

public class ScannerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private Button btClick;
    private TextView textCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_scanner );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make( view,"Para maiores informações: leandro.hdsl@gmail.com",Snackbar.LENGTH_LONG )
                        .setAction( "Action",null ).show();
            }
        } );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        textCode = (TextView) findViewById( R.id.textCode );
        btClick = (Button) findViewById( R.id.btClick );
        btClick.setOnClickListener( this );

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult( requestCode,resultCode,data );

        //IntentResult resultScanning = IntentIntegrator.parseActivityResult(resultCode, requestCode, data);
        Log.e("Erro !!!" , "resultCode >> " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK){
                final String contents = data.getStringExtra( "SCAN_RESULT" );
                final String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText( getApplication(), "Código: " + contents, Toast.LENGTH_LONG ).show();
                textCode.setText( "Código: " + contents + "\nFormato: " + format);

                //startActivity( data );

            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText( getApplicationContext(), "Sem Código !", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.scanner,menu );
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

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    public void alertaDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder( ScannerActivity.this );
        builder.setTitle( "Texto de alerta do aplicativo" )
                .setMessage( "Codigo: " + IntentIntegrator.REQUEST_CODE)
                .setPositiveButton( "Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i) {
                        String dataResult = Integer.toString( IntentIntegrator.REQUEST_CODE );
                        textCode.setText( dataResult );
                    }
                } ).setNegativeButton( "Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,int i) {
                //
            }
        } );
        builder.create();
        builder.show();
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent( "com.google.zxing.client.android.SCAN" );
        intent.setPackage( "com.google.zxing.client.android" );
        intent.putExtra( "SCAN_MODE", 0 );
        try {
            startActivityForResult( intent, 0 );

        } catch (ActivityNotFoundException aex){
            aex.printStackTrace();// imprime o erro excessao
            AlertDialog.Builder builder = new AlertDialog.Builder( this );
            builder.setTitle( "Aplicção não encontrada" );
            builder.setMessage( "Nós não encontramos a aplicação Scan QR Code."
                    + " Gostaria de realizar o Downolad do Android Market" );
            builder.setPositiveButton( "Sim",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface,int i) {
                    Intent marketIntent = new Intent( Intent.ACTION_VIEW );
                    marketIntent.setData( Uri.parse( "market://details?id=com.google.zxing.client.android" ) );
                    startActivity( marketIntent );
                }
            } ).create();
            builder.setNegativeButton( "Não", null ).create();
            builder.show();
        }
    }
}
