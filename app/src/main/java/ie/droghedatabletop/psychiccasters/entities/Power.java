package ie.droghedatabletop.psychiccasters.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Power {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "power_name")
    private String powerName;

    private int powerValue;

    public Power(@NonNull String powerName, int powerValue) {
        this.powerName = powerName;
        this.powerValue = powerValue;
    }

    @NonNull
    public String getPowerName() {
        return powerName;
    }

    public int getPowerValue() {
        return powerValue;
    }
}
