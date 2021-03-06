package cg.computergraphics;

import android.content.pm.ActivityInfo;
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
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        myview = (MyView) findViewById(R.id.myview);
        myview.setBitmapScale(appSettings.getBitmapScale());
        myview.setDrawingTool(appSettings.getDefaultTool());


        //alert dialogs manager
        dialogWindowManager = new DialogWindowManager(MainActivity.this);

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
                        new SectionDrawerItem().withDivider(false).withName("Tools"),
                        new PrimaryDrawerItem().withIdentifier(1).withName("Brush").withIcon(R.drawable.ic_gesture),
                        new SwitchDrawerItem().withIdentifier(2).withName("Line").withIcon(R.drawable.ic_vector_line)
                                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                        appSettings.setLineColorApprox(isChecked);
                                    }
                                }),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Bezier curve").withIcon(R.drawable.ic_curve),
                        new PrimaryDrawerItem().withSelectable(false).withName("Mosaic").withIcon(R.drawable.ic_mosaic)
                                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        dialogWindowManager.showDialog(DialogWindowManager.IDD_MOSAIC);
                                        return false;
                                    }
                                }),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Fill").withIcon(R.drawable.ic_fill),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withSelectable(false).withName("Splines").withIcon(R.drawable.ic_vector_curve)
                                .withSubItems(
                                        new SecondaryDrawerItem().withIdentifier(9).withName("Hermite curve").withIcon(R.drawable.ic_spline_elem),
                                        new SecondaryDrawerItem().withIdentifier(10).withName("B-Spline").withIcon(R.drawable.ic_spline_elem),
                                        new SecondaryDrawerItem().withIdentifier(11).withName("NURBSpline").withIcon(R.drawable.ic_spline_elem)
                                ),
                        new SectionDrawerItem().withDivider(true).withName("Figures"),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Circle").withIcon(R.drawable.ic_vector_circle),
                        new PrimaryDrawerItem().withIdentifier(6).withName("Rectangle").withIcon(R.drawable.ic_rectangle),
                        new SwitchDrawerItem().withIdentifier(7).withName("Polygon").withIcon(R.drawable.ic_polygon)
                                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                        appSettings.setPolygonColorApprox(isChecked);
                                    }
                                }),
                        new PrimaryDrawerItem().withIdentifier(8).withName("KR Figure").withIcon(R.drawable.ic_figure),
                        new SwitchDrawerItem().withSelectable(false).withName("With filling")
                                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                        appSettings.setFill(isChecked);
                                    }
                                })
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.isSelected()) {
                            myview.getDrawingTool().transferToMainBitmap();
                            myview.setDrawingTool((int) drawerItem.getIdentifier());
                            return false;
                        } else return true;
                    }
                })
                //.withDrawerWidthPx(170)
                .build();

        sideBar.setSelection(appSettings.getDefaultTool());
        sideBar.addStickyFooterItem(
                new SwitchDrawerItem().withSelectable(false).withName("Scroll").withIcon(R.drawable.ic_scroll)
                        .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                appSettings.setScroll(isChecked);
                            }
                        })
        );

        //color picker button
        FloatingActionButton colorPickerButton = (FloatingActionButton) findViewById(R.id.color_picker_button);
        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogWindowManager.showDialog(DialogWindowManager.IDD_COLOR_PICKER);
            }
        });

        //clean button
        FloatingActionButton cleanButton = (FloatingActionButton) findViewById(R.id.clean_button);
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myview.cleanScreen();
            }
        });
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
