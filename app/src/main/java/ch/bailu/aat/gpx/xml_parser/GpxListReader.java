package ch.bailu.aat.gpx.xml_parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.AltitudeDelta;
import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.xml_parser.parser.AbsXmlParser;
import ch.bailu.aat.services.background.ThreadControl;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.parser.OnParsedInterface;

public class GpxListReader {
    private final ThreadControl threadControl;

    private final OnParsed way;
    private final OnParsed track;
    private final OnParsed route;

    private final AbsXmlParser parser;


    public GpxListReader(Foc in, AutoPause pause) throws IOException, SecurityException, XmlPullParserException {
        this(ThreadControl.KEEP_ON, in, pause);
    }


    public GpxListReader (ThreadControl c, Foc in, AutoPause pause)
            throws IOException, SecurityException, XmlPullParserException {

        track = new OnParsed(
                GpxType.TRACK,
                new MaxSpeed.Samples(),
                pause,
                new AltitudeDelta.LastAverage());

        way = new OnParsed(
                GpxType.WAY,
                MaxSpeed.NULL,
                AutoPause.NULL,
                AltitudeDelta.NULL);


        route = new OnParsed(
                GpxType.ROUTE,
                MaxSpeed.NULL,
                AutoPause.NULL,
                new AltitudeDelta.LastAverage());

        threadControl=c;

        parser = new XmlParser(in);
        parser.setOnRouteParsed(route);
        parser.setOnTrackParsed(track);
        parser.setOnWayParsed(way);
        parser.parse();
        parser.close();
    }


    public GpxList getGpxList() {
        if (track.hasContent()) return track.getGpxList();
        if (route.hasContent()) return route.getGpxList();
        return way.getGpxList();
    }


    
    private class OnParsed implements OnParsedInterface {
        private final GpxList gpxList;
        private boolean  haveNewSegment=true;

        public OnParsed(GpxType type, MaxSpeed speed, AutoPause pause, AltitudeDelta altitude) {
            gpxList = new GpxList(type, speed, pause, altitude);
        }


        public GpxList getGpxList() {
            return gpxList;
        }

        public boolean hasContent() {
            return gpxList.getPointList().size()>0;
        }

        @Override
        public void onHaveSegment() {
            haveNewSegment=true;
        }

        @Override
        public void onHavePoint() throws IOException {
            if (threadControl.canContinue()) {
                if (haveNewSegment) {
                    gpxList.appendToNewSegment(new GpxPoint(parser), 
                            parser.getAttributes());
                    haveNewSegment=false;
                } else {
                    gpxList.appendToCurrentSegment(new GpxPoint(parser), 
                            parser.getAttributes());

                }

            } else {
                throw new IOException();
            }
        }
    }
}
