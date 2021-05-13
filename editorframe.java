import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class editorframe extends JFrame implements ActionListener {
    public DefaultTableModel model = new DefaultTableModel();
    JScrollPane scrollPane;
    JTable table;
    JToolBar tool = new JToolBar();
    JButton delete = new JButton("删除");
    JButton add = new JButton("添加");
    JButton quit = new JButton("退出");
    Date day = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当前时间
    String filepath;
    public editorframe(String filepath) {
        this.filepath = filepath;
        this.setLayout(null);
        try {
            model = new test().read(model, filepath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        table = new JTable(model);
        //table.setRowHeight(22);
        table.setGridColor(Color.black);
        table.setPreferredScrollableViewportSize(new Dimension(600, 350));
        table.setFillsViewportHeight(true);
         table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS );//不能调节大小
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //每次只选一行
        //table.addPropertyChangeListener(this);
        Action action = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                CellListener cl = (CellListener)e.getSource();
                System.out.println(df.format(day)+": 第" + cl.getRow()+1+"行" +"第"+cl.getColumn()+1+"列"
                       + cl.getOldValue()+"修改为： " + cl.getNewValue());
                postlog(df.format(day)+": 第" + cl.getRow()+1+"行" +"第"+cl.getColumn()+1+"列"
                        + cl.getOldValue()+"修改为： " + cl.getNewValue());
            }
        };
        CellListener cl = new CellListener(table, action);
        //setTitle("");
        setSize(600, 400);
        setVisible(true);

        tool.add(delete);
        tool.add(add);
        tool.add(quit);
        tool.setSize(600, 50);
        this.add(tool);
        tool.setBounds(0, 0, 600, 30);
        this.add(scrollPane);
        scrollPane.setBounds(0, 30, 600, 370);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        delete.addActionListener(this);
        add.addActionListener(this);
        quit.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {
        int Index = table.getSelectedRow();

        if (e.getSource() == delete) {
            String str = "";
            for (int i = 0; i < table.getColumnCount(); i++) {
                // str+=String.valueOf(table.getValueAt(table.getSelectedRow(),i))+'\t';//对象obj为null，转换结果为字符串"null"
                Object obj = table.getValueAt(Index, i);
                if (obj == null) {
                    str += "\t" + "\t";
                } else {
                    str += obj.toString() + "\t";
                }
            }
            int temp=Index+1;
            postlog(df.format(day)+"：删除第"+temp+"行:"+str);
            System.out.println(df.format(day)+"：删除第"+temp+"行:"+str);
            model.removeRow(Index);
            table.setModel(model);
            return;
        }
        if (e.getSource() == add) {
            Object cloumnsname[] = new Object[model.getColumnCount() + 1];
            ((DefaultTableModel) table.getModel()).insertRow(table.getRowCount(), cloumnsname); //添加一行
            table.setModel(model);
            String str=df.format(day)+"：增加一行";
            postlog(str);
            System.out.println(str);
            return;
        }
        if (e.getSource() == quit) {
            System.exit(0);
            return;
        }
    }
    public void postlog(String log ){
        HttpURLConnection conn = null;
        try {
            // 创建一个URL对象
            URL URL = new URL("http://localhost:5000/transactions/data");
            conn = (HttpURLConnection) URL.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);// 设置此方法,允许向服务器输出内容
            conn.setDoInput(true);
            OutputStreamWriter out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
           // String str="{\"set\": \"1234\",\n\"signature\": \"0x1289471984\",\n\"data\":\""+log+ "\"\n}";
            String str="{\"set\": \"1234\",\n\"signature\": \"0x1289471984\",\n\"data\":1234\n}";
            //String str="{\"set\": \"1234\",\n\"signature\": \""+log+"\",\n\"data\":1234\n}";
            out.write(str);
            // flush输出流的缓冲
            out.flush();
            /*OutputStream ops = conn.getOutputStream();
            PrintStream ps=new PrintStream(ops);
            ps.println(log);
            ps.flush();
            ps.close();*/
            int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
            if (responseCode == 200) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String string=bf.readLine();
              //  System.out.println(string);

            } else {
//                Log.i(TAG, "访问失败" + responseCode);
                System.out.print("访问失败");
            }
            //conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();// 关闭连接
            }
        }

    }

}


//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);