package ie.droghedatabletop.psychiccasters.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "caster")
public class Caster {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "caster_name")
    private String casterName;

    @ColumnInfo(name = "powers")
    private String powers;

    @Ignore
    public Caster(@NonNull String casterName) {
        this.casterName = casterName;
    }

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
