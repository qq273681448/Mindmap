package com.xlm1.mindmap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int[] tree_xnum = new int[100];
    private RelativeLayout insertLayout;
    private DrawGeometryView view;

    private HVScrollView hv;
    private Button[] bt = new Button[15];
    private RelativeLayout.LayoutParams[] layoutParams = new RelativeLayout.LayoutParams[15];
    private RelativeLayout.LayoutParams[] layoutParams1 = new RelativeLayout.LayoutParams[15];
    private Mystack mstack = new Mystack();
    private boolean model = true;
    private int bt_width = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView murp_nodemodel_title = (TextView) findViewById(R.id.murp_nodemodel_title);
        murp_nodemodel_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), !model ? "已切换到擦除模式，点击节点会擦除后面节点，赶快试试吧。" : "已切换到正常模式，所有节点在一张图上，赶快试试吧。", Toast.LENGTH_LONG).show();
                model = !model;
            }
        });


        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        insertLayout = (RelativeLayout) findViewById(R.id.layout_zone);

        hv = (HVScrollView) findViewById(R.id.hvscrollview);

        nodechild[] nc = new nodechild[1];
        nc[0] = new nodechild("2", "思维导图");
        drawbutton(width - 200, 50, 50, 1, nc, "1");
    }

    public class nodechild {
        private String id;
        private String name;
        private String buteid;
        private String butetype;
        private String nodetype;
        private String ispass;

        public String getNodetype() {
            return nodetype;
        }

        public void setNodetype(String nodetype) {
            this.nodetype = nodetype;
        }

        public nodechild(String id, String name, String buteid, String butetype, String nodetype) {
            super();
            this.id = id;
            this.name = name;
            this.buteid = buteid;
            this.butetype = butetype;
            this.nodetype = nodetype;

        }

        public nodechild(String id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        public nodechild(String id, String name, String ispass) {
            super();
            this.id = id;
            this.name = name;
            this.ispass = ispass;
        }

        public String getIspass() {
            return ispass;
        }

        public void setIspass(String ispass) {
            this.ispass = ispass;
        }

        public String getButeid() {
            return buteid;
        }

        public void setButeid(String buteid) {
            this.buteid = buteid;
        }

        public String getButetype() {
            return butetype;
        }

        public void setButetype(String butetype) {
            this.butetype = butetype;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void drawbutton(int button_y, int button_x, int line_x, final int tree_current, final nodechild[] nc, String nodeid) {
//        存储线的起点y坐标
        int line_y = button_y;
//        这个只是为了区分业务中偶数层button宽度为300，齐数层为200
        button_x = tree_current % 2 == 1 ? button_x : button_x - 100;
//        得到下一层级需要绘制的数量
        int num = 1;
        if (tree_current != 1) num = nc.length;// 下一层个数
//        得到下一级第一个按钮的y坐标
        button_y = button_y - (num - 1) * bt_width / 2;
        if (button_y < tree_xnum[tree_current]) {
            button_y = tree_xnum[tree_current] + 100;
        }
//        移动当前布局到页面中心
        if (tree_current > 2) hv.scrollTo(button_x - 400, button_y - 100);
        if (tree_xnum[tree_current] < button_y + 200 + (num - 1) * bt_width)
            tree_xnum[tree_current] = button_y + 200 + (num - 1) * bt_width;
//        存储下一级首个button坐标
        final int button_y_f = button_y;
        final int button_x_f = button_x;
        for (int i = 0; i < num; i++) {
            final int bt_paly_y = bt_width;
            int bt_w = tree_current % 2 == 0 ? bt_width : 200;
            int bt_h = 200;
//            定义及设置button属性
            bt[i] = new Button(this);
            if (tree_current % 2 != 0) {
                bt[i].setBackgroundResource(R.drawable.allokbutton);
            } else {
                bt[i].setBackgroundResource(R.drawable.button33);
            }
            bt[i].setTextColor(Color.WHITE);
            bt[i].setTextSize(15 - (int) Math.sqrt(nc[i].getName().length() - 1));
            bt[i].setText(nc[i].getName());
//            定义及设置出场动画
            final String nc_id = nc[i].getId();
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setInterpolator(new BounceInterpolator());
            animation.setStartOffset(tree_current == 1 ? 1050 : 50);// 动画秒数。
            animation.setFillAfter(true);
            animation.setDuration(700);
            bt[i].startAnimation(animation);
            final int i1 = i;
//            设置监听
            bt[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    如果是擦除模式，擦除其他同级节点及线条
                    if (model) mstack.pop(tree_current);
//                    防止多次点击，偷懒的解决办法
                    if (((Button)v).getHint() != null) {
                        Toast.makeText(getApplicationContext(), ((Button)v).getText(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    ((Button)v).setHint("1");
                    insertLayout.setEnabled(false);
                    int w = button_y_f + i1 * bt_paly_y;
                    int h = button_x_f + bt_paly_y / 2 * 3;
                    getRemoteInfo(w, h, button_y_f + i1 * bt_paly_y, button_x_f, tree_current + 1, nc_id,
                            nc[i1].getButeid());
                }
            });
//            把button通过布局add到页面里
            layoutParams[i] = new RelativeLayout.LayoutParams(bt_w, bt_h);
            layoutParams[i].topMargin = button_y + i * bt_paly_y;
            layoutParams[i].leftMargin = button_x;
            insertLayout.addView(bt[i], layoutParams[i]);

//            把线绘制到页面里
            if (tree_current != 1) {
                if (button_y + 100 + i * 300 - (line_y + 100) >= 0) {//为了优化内存，也是醉了
                    view = new DrawGeometryView(this, 50, 50, button_x + 100 - (line_x + bt_paly_y) + 50 + (tree_current % 2 == 0 ? 100 : 0), button_y + 100 + i * 300
                            - (line_y + 100) + 50, nc[i].getButetype());
                    layoutParams1[i] = new RelativeLayout.LayoutParams(Math.abs(line_x - button_x) + 500, 100 + button_y + i * 300 - line_y);
                    view.invalidate();
                    layoutParams1[i].topMargin = (line_y + 100) - 50;// line_y-600;//Math.min(line_y+100,button_y+100
                    layoutParams1[i].leftMargin = (line_x + bt_paly_y) - 50;// line_x+300;
                    if (tree_current % 2 == 0) layoutParams1[i].leftMargin -= 100;
                    insertLayout.addView(view, layoutParams1[i]);
                } else {
                    view = new DrawGeometryView(this, 50, -(button_y + 100 + i * 300 - (line_y + 100)) + 50, button_x - line_x - 150 + (tree_current % 2 == 0 ? 100 : 0), 50,
                            nc[i].getButetype());
                    layoutParams1[i] = new RelativeLayout.LayoutParams(Math.abs(line_x - button_x) + 500, 100 + Math.abs(button_y + i * 300
                            - line_y));
                    view.invalidate();
                    layoutParams1[i].topMargin = (button_y + 100 + i * 300) - 50;// line_y-600;//Math.min(line_y+100,button_y+100
                    layoutParams1[i].leftMargin = (line_x + bt_paly_y) - 50;// line_x+300;
                    if (tree_current % 2 == 0) layoutParams1[i].leftMargin -= 100;
                    insertLayout.addView(view, layoutParams1[i]);
                }
//                line入栈
                mstack.push(view, tree_current);
            }
//            button入栈
            mstack.push(bt[i], tree_current);
        }
    }

    public synchronized void getRemoteInfo(int paly_y, int paly_x, int ppaly_y, int ppaly_x, int tree_h,
                                           String nodeid, String buteid) {
        int n = 1;
        Random random = new Random();
        n = random.nextInt(4) + 1;
        nodechild[] nc = new nodechild[n];
        for (int i = 0; i < n; i++) {
            nc[i] = new nodechild("1", "你好");
        }
        drawbutton(paly_y, paly_x, ppaly_x, tree_h, nc, nodeid);
    }


    public class Mystack {
        View[] v = new View[1500];
        int[] treehigh = new int[1500];
        int size = 0;

        public void push(View view, int treecurrent) {
            size++;
            v[size] = view;
            treehigh[size] = treecurrent;
        }

        public void pop(int treecurrent) {
            while (treehigh[size] > treecurrent && size > 0) {
                if (size > 0) insertLayout.removeView(v[size]);
                size--;
            }
            for (int j = 49; j > treecurrent; j--) {//树高清0
                tree_xnum[j] = 0;
            }
            for (int x = size; x > 0; x--) {
                if (treehigh[x] > treecurrent) {
                    insertLayout.removeView(v[x]);
                }//修复栈顶元素被前一层树元素占用bug，但是会浪费少量内存，考虑到内存很小，暂时不优化吧。
                if (treehigh[x] == treecurrent) {
                    try {
                        ((Button) v[x]).setHint(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
