package net.anguslong.todolist;

import java.util.List;

import net.anguslong.todolist.model.Task;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {

  private final List<Task> list;
  private final Activity context;

  public TaskAdapter(Activity context, List<Task> list) {
    super(context, R.layout.list_item_task, list);
    this.context = context;
    this.list = list;
  }

  static class ViewHolder {
    protected TextView text;
    protected CheckBox checkbox;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = null;
    if (convertView == null) {
      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.list_item_task, null);
      final ViewHolder viewHolder = new ViewHolder();
      viewHolder.text = (TextView) view.findViewById(R.id.crime_list_item_titleTextView);
      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.crime_list_item_solvedCheckBox);
      viewHolder.checkbox
          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
              Task element = (Task) viewHolder.checkbox
                  .getTag();
              element.setComplete(buttonView.isChecked());
              Log.d("TaskListFragment", "Checkbox tag: " + buttonView.isChecked());

            }
          });
      view.setTag(viewHolder);
      viewHolder.checkbox.setTag(list.get(position));
    } else {
      view = convertView;
      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
    }
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.text.setText(list.get(position).getTitle());
    holder.checkbox.setChecked(list.get(position).isComplete());
    return view;
  }
} 
