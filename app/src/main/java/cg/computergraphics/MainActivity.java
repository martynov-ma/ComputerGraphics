package cg.computergraphics;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity {

    private MyView myview;
    private Drawer sideBar;

    public static AppSettings appSettings;

    private DialogWindowManager dialogWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //app settings
        appSettings = new AppSettings();

        setContentView(R.layout.activity_main);
        myview = (MyView) findViewById(R.id.myview);
        myview.setDrawingTool(1);

        //alert dialogs manager
        dialogWindowManager = new DialogWindowManager(MainActivity.this);

        //clean button
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.clean_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myview.cleanScreen();
            }
        });

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ComputerGraphics");
        setSupportActionBar(toolbar);

        //sidebar
        sideBar = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.drawer_header)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Brush").withIcon(R.drawable.ic_gesture),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Line").withIcon(R.drawable.ic_vector_line),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Circle").withIcon(R.drawable.ic_vector_circle2),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Bezier curve").withIcon(R.drawable.ic_vector_curve),
                        new PrimaryDrawerItem().withSelectable(false).withName("Mosaic").withIcon(R.drawable.ic_grid)
                                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        dialogWindowManager.showDialog(DialogWindowManager.IDD_MOSAIC);
                                        return true;
                                    }
                                }),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withSelectable(false).withName("Color").withIcon(R.drawable.ic_palette)
                                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        dialogWindowManager.showDialog(DialogWindowManager.IDD_COLOR_PICKER);
                                        return true;
                                    }
                                }),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withSelectable(false).withName("Scroll").withIcon(R.drawable.ic_meteor)
                                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                        myview.setScroll(isChecked);
                                        sideBar.closeDrawer();
                                    }
                                })
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.isSelected()) myview.setDrawingTool((int) drawerItem.getIdentifier());
                        return false;
                    }
                })
                //.withDrawerWidthPx(170)
                .build();

    }

    public MyView getMyView() {
        return myview;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menuitem_readfile:
                dialogWindowManager.showDialog(DialogWindowManager.IDD_OPEN_FILE);
                break;
            case R.id.menuitem_writefile:
                dialogWindowManager.showDialog(DialogWindowManager.IDD_SET_FILE_NAME);
                break;
            case R.id.menuitem_scale:
                dialogWindowManager.showDialog(DialogWindowManager.IDD_SET_SCALE);
                break;
            case R.id.menuitem_settings:
                dialogWindowManager.showDialog(DialogWindowManager.IDD_SETTINGS);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(sideBar.isDrawerOpen()) {
            sideBar.closeDrawer();
        }
        else {
            super.onBackPressed();
        }
    }
}
