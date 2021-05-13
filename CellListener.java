import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CellListener implements PropertyChangeListener, Runnable
{
    private JTable table;
    private Action action;
    private int row;
    private int column;
    private Object oldValue;
    private Object newValue;
    public CellListener(JTable table, Action action)
    {
        this.table = table;
        this.action = action;
        this.table.addPropertyChangeListener( this );
    }
    private CellListener(JTable table, int row, int column, Object oldValue, Object newValue)
    {
        this.table = table;
        this.row = row;
        this.column = column;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public int getColumn()
    {
        return column;
    }
    public Object getNewValue()
    {
        return newValue;
    }
    public Object getOldValue()
    {
        return oldValue;
    }
    public int getRow()
    {
        return row;
    }
    public JTable getTable()
    {
        return table;
    }
    public void propertyChange(PropertyChangeEvent e)
    {
        if ("tableCellEditor".equals(e.getPropertyName()))
        {
            if (table.isEditing()){
                processEditingStarted();
            }
            else{
                processEditingStopped();
            }

        }
    }
    private void processEditingStarted()
    {
        SwingUtilities.invokeLater( this );
    }

    public void run()
    {
        row = table.convertRowIndexToModel( table.getEditingRow() );
        column = table.convertColumnIndexToModel( table.getEditingColumn() );
        oldValue = table.getModel().getValueAt(row, column);
        //这里应对oldValue为null的情况做处理，否则将导致原值与新值均为空时仍被视为值改变
        if(oldValue == null)
            oldValue = "";
        newValue = null;
    }

    private void processEditingStopped()
    {
        newValue = table.getModel().getValueAt(row, column);
        if(newValue == null)
            newValue = "";
        if (! newValue.equals(oldValue))
        {
            CellListener tcl = new CellListener(
                    getTable(), getRow(), getColumn(), getOldValue(), getNewValue());

            ActionEvent event = new ActionEvent(
                    tcl,
                    ActionEvent.ACTION_PERFORMED,
                    "");
            action.actionPerformed(event);
        }
    }
}