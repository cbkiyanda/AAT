package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;


public class GpxBigDelta implements GpxBigDeltaInterface {
    public final static GpxBigDelta NULL= new GpxBigDelta(
            MaxSpeed.NULL, AutoPause.NULL, AltitudeDelta.NULL);

    private final MaxSpeed maximumSpeed;
    private final AutoPause autoPause;
    private final AltitudeDelta altitudeDelta;

    private float distance=0;

    private long startTime=0;
    private long endTime=0;
    private long pause=0;

    private GpxType type;

    private BoundingBoxE6 boundingBox = null;


    public GpxBigDelta(MaxSpeed max, AutoPause pause, AltitudeDelta altitude) {
        maximumSpeed = max;
        autoPause = pause;
        altitudeDelta = altitude;
    }



    public void update(GpxPointNode p) {
        _update(p);

        if (autoPause.update(p)) {
            altitudeDelta.add(p.getAltitude(), p.getDistance());
        }
    }


    public void updateWithPause(GpxPointNode p) {
        if (getEndTime()!=0) {
            long pause = p.getTimeStamp()-getEndTime();
            if (pause > 0) incPause(pause);
        }
        _update(p);
    }

    private void _update(GpxPointNode p) {
        setStartTime(p.getTimeStamp());
        setEndTime(p.getTimeStamp());

        incDistance(p.getDistance());

        addBounding(p.getLatitudeE6(), p.getLongitudeE6());
        setMaximumSpeed(p.getSpeed());
    }

    public void updateWithPause(GpxBigDeltaInterface delta) {
        setStartTime(delta.getStartTime());

        incPause(delta.getPause());
        incEndTime(delta.getTimeDelta()+delta.getPause());
        incDistance(delta.getDistance());

        addBounding(delta.getBoundingBox());
        setMaximumSpeed(delta.getSpeed());
    }


    private void setStartTime(long timestamp) {
        if (startTime==0) {
            startTime = timestamp;
            endTime = timestamp;
        }
    }

    private void incEndTime(long t) {
        endTime += t;
    }

    private void setEndTime(long timestamp) {
        endTime = timestamp;
    }

    private void setMaximumSpeed(float speed) {
        maximumSpeed.add(speed);
    }

    private void incPause(long p) {
        pause += p;
    }

    private void incDistance(float d) {
        distance += d;
    }

    private void addBounding(BoundingBoxE6 b) {
        if (boundingBox == null) {
            boundingBox = new BoundingBoxE6(b);
        } else {
            boundingBox.add(b);
        }
    }

    private void addBounding(int la, int lo) {
        if (boundingBox == null) {
            boundingBox = new BoundingBoxE6(la,lo);
        } else {
            boundingBox.add(la,lo);
        }
    }


    public BoundingBoxE6 getBoundingBox() {
        if (boundingBox==null) return BoundingBoxE6.NULL_BOX;
        return boundingBox;
    }


    public float getSpeed() {
        float average;
        float sitime = ((float)getTimeDelta()) / 1000f;

        if (sitime > 0f) average = distance / sitime;
        else average=0f;

        return average;
    }


    public float getMaximumSpeed() { 
        return maximumSpeed.get();
    }

    public float getDistance() { 
        return distance; 
    }

    public long getTimeDelta() { 
        return (endTime-startTime)-pause;
    }


    public long getPause() {
        return pause;
    }

    @Override
    public long getAutoPause() {
        return autoPause.get();
    }

    @Override
    public short getAscend() {
        return altitudeDelta.getAscend();
    }

    @Override
    public short getDescend() {
        return altitudeDelta.getDescend();
    }

    @Override
    public short getSlope() {
        return altitudeDelta.getSlope();
    }


    public long getStartTime() { 
        return startTime;
    }


    public float getAcceleration() {
        return 0;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }


    public void setType(GpxType t) {
        type = t;
    }


    @Override
    public GpxType getType() {
        return type;
    }
}
