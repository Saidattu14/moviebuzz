package com.example.moviebuzz.ui.tickets;

import java.util.ArrayList;
import java.util.List;

public class Seating {


    List<Seating1> list;

    public Seating() {
        this.list = new ArrayList<>();
    }

    public List<Seating1> getList() {
        return list;
    }

    public void addSeatDetails(String row_id, String seat_number)
    {
      for(int i=0; i<this.list.size();i++)
      {
          if(list.get(i).row_id.equals(row_id))
          {
              this.list.get(i).seat_numbers.add(seat_number);
          }
      }
    }

    public void deleteSeatDetails(String row_id, String seat_number)
    {
        for(int i=0; i<this.list.size();i++)
        {
            if(list.get(i).row_id.equals(row_id))
            {
                this.list.get(i).seat_numbers.remove(seat_number);
            }
        }
    }



    public void addRows(String row_id)
    {
      this.list.add(new Seating1(row_id,new ArrayList<>()));
    }
}
