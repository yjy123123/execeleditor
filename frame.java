import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frame extends JFrame implements ActionListener{
    public JButton putin =new JButton("导入");
    public JButton quit   =new JButton("退出");
    public frame(){
        this.setLayout(null);
        putin.setBounds(60,30,80,50);
        quit.setBounds(60,100,80,50);
        this.add(putin);
        this.add(quit);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(200,200);
        putin.addActionListener(this);
        quit.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e){
       if(e.getSource()== putin){
           String filepath = JOptionPane.showInputDialog("请输入Excel文件路径");
           new editorframe(filepath);
            this.setVisible(false);
        }
        if(e.getSource()==quit){
            System.exit(0);
        }

    }
    public static  void main (String arg[]){
        new frame();
    }
}
