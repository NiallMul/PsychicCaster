package ie.droghedatabletop.psychiccasters.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "caster")
public class Caster {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "caster_name")
    private String casterName;

    @ColumnInfo(name = "powers")
    private String powers;

    public Caster(@NonNull String casterName, String powers) {
        this.casterName = casterName;
        this.powers = powers;
    }

    @NonNull
    public String getCasterName() {
        return casterName;
    }

    public String getPowers() {
        return powers;
    }
}
