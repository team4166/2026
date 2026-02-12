import edu.wpi.first.cameraserver;
import edu.wpi.first.wpilib.Timedrobot;

/**
 * uses the cameraserverclass to automaticlly capture video from a usb webcam and send it to the
 * FRC dashboard without doing any virsion processing. This is the easiest way to get camera images
 * to the dashboard. Just add this to the robot constructor.
 */
public class robot extends timed robot {
    public robot(){
        CameraServer.startautomaticcapture();
    }
}
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
public class  robot extends Timerobot {
    thread m_visionThread;

    public robot () {
        m_visionTread =
            new Thread(
                ()-> {
                    //get the UsbCamera from CameraServer
                    UsbCamera camera = CameraServer.startautomaticCapture();
                    //Set the resolution
                    //camera.setResolution(640,480);

                    //get a CvSource. 
                }
            )
    }
}