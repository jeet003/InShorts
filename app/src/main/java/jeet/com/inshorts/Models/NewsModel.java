package jeet.com.inshorts.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeet on 10/9/17.
 */

/*
{
      "ID":303,
      "TITLE":"Japan struggles to understand bitcoin after Mt.Gox collapse",
      "URL":"http://www.afr.com/p/technology/japan_struggles_to_understand_bitcoin_JywRYTHI2hsbTA2C1WaLzL",
      "PUBLISHER":"The Australian Financial Review",
      "CATEGORY":"b",
      "HOSTNAME":"www.afr.com",
      "TIMESTAMP":1394474340759
   }
 */

public class NewsModel implements Parcelable{
    private String ID,TITLE,URL,PUBLISHER,CATEGORY,HOSTNAME,TIMESTAMP;

    public NewsModel(){

    }

    protected NewsModel(Parcel in)
    {
        ID=in.readString();
        TITLE=in.readString();
        URL=in.readString();
        PUBLISHER=in.readString();
        CATEGORY=in.readString();
        HOSTNAME=in.readString();
        TIMESTAMP=in.readString();
    }
    public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
        }
    };

    public String getID()
    {
        return ID;
    }
    public void setID(String ID)
    {
        this.ID=ID;
    }
    public String getTITLE()
    {
        return TITLE;
    }
    public void setTITLE(String TITLE)
    {
        this.TITLE=TITLE;
    }
    public String getURL()
    {
        return URL;
    }
    public void setURL(String URL)
    {
        this.URL=URL;
    }
    public String getPUBLISHER()
    {
        return PUBLISHER;
    }
    public void setPUBLISHER(String PUBLISHER)
    {
        this.PUBLISHER=PUBLISHER;
    }
    public String getCATEGORY()
    {
        return CATEGORY;
    }
    public void setCATEGORY(String CATEGORY)
    {
        this.CATEGORY=CATEGORY;
    }
    public String getHOSTNAME()
    {
        return HOSTNAME;
    }
    public void setHOSTNAME(String HOSTNAME)
    {
        this.HOSTNAME=HOSTNAME;
    }
    public String getTIMESTAMP()
    {
        return TIMESTAMP;
    }
    public void setTIMESTAMP(String TIMESTAMP)
    {
        this.TIMESTAMP=TIMESTAMP;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(TITLE);
        dest.writeString(URL);
        dest.writeString(PUBLISHER);
        dest.writeString(CATEGORY);
        dest.writeString(HOSTNAME);
        dest.writeString(TIMESTAMP);
    }
}
