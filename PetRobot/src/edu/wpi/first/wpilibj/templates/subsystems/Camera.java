
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Camera extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    AxisCamera iSpy;
    CriteriaCollection cc;      // the criteria for doing the particle filter operation
    int Xcoord = 160;   // point for driveWithCamera to move according to
    int targetWidth = 0;   
    int targetHeight = 0;  
    double distance = 0;   // point for driveWithCamera to move according to
    int bestSoFar = 0;
    
    final int Y_IMAGE_RES = 240;		//Y Image resolution in pixels, should be 120, 240 or 480
    final double VIEW_ANGLE = 37.4;  //camera angle from center of field of view to edge

    //Score limits used for target identification
    final int  RECTANGULARITY_LIMIT = 40;
    final int ASPECT_RATIO_LIMIT = 40;

    //Score limits used for hot target determination
    final int TAPE_WIDTH_LIMIT = 50;
    final int  VERTICAL_SCORE_LIMIT = 50;
    final int LR_SCORE_LIMIT = 50;

    //Minimum area of particles to be considered
    final int AREA_MINIMUM = 50;    
    //Maximum number of particles to process
    final int MAX_PARTICLES = 8;
    int pictures = 0;
    int cycles = 0;
    
    public Camera() {
        iSpy = AxisCamera.getInstance();  // get an instance of the camera
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, AREA_MINIMUM, 65535, false); // criteria of non-miniscule particles
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
//        setDefaultCommand(new ISpy());
    }
    // Fine, have it your way. What were you doing then?
    public void Sneeeeking() {
        double topScore = 0;
            try {   
                TargetReport target = new TargetReport();
                int verticalTargets[] = new int[MAX_PARTICLES];
                int horizontalTargets[] = new int[MAX_PARTICLES];
                int verticalTargetCount, horizontalTargetCount;
                pictures += 1;
                ColorImage image = iSpy.getImage();     // comment if using stored images
                BinaryImage thresholdImage = image.thresholdRGB(160,255,235,255,235,255);   // keep only lamp in sample picture
//                thresholdImage.write("/CameraPictures/ColorResult"+pictures+".bmp"); // write output to cRIO for FTP retrieval
                BinaryImage filteredImage = thresholdImage.particleFilter(cc);           // filter out small particles               
                filteredImage.write("/CameraPictures/FilterManualResult"+pictures+".bmp");

                //iterate through each particle and score to see if it is a target
                Scores scores[] = new Scores[filteredImage.getNumberParticles()]; //makes an empty list with slots for all the particles
                horizontalTargetCount = verticalTargetCount = 0;
                
                if(filteredImage.getNumberParticles() > 0){
                    for (int particleNum = 0; particleNum < MAX_PARTICLES && particleNum < filteredImage.getNumberParticles(); particleNum++) {
                        ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(particleNum); // generates a report of the different aspects of the particle                        
                        scores [particleNum] = new Scores(); //makes each slot the class "Scores" which will have properties, grading different aspects of the report

                        // uses these functions to grade different parts of the report
                        scores [particleNum].rectangularity = scoreRectangularity(report); 
                        scores [particleNum].aspectRatioVertical = scoreAspectRatio(filteredImage, report, particleNum, true);
                        scores [particleNum].aspectRatioHorizontal = scoreAspectRatio(filteredImage, report, particleNum, false);
                        //Check if the particle is a horizontal target. If so, announce it and add it to a list of targets
                        if(scoreCompare(scores [particleNum], false)) {                            
                            if ((scores [particleNum].rectangularity+scores [particleNum].aspectRatioHorizontal) > topScore) {
                                topScore = scores [particleNum].rectangularity+scores [particleNum].aspectRatioHorizontal;                                
                                bestSoFar = report.center_mass_x;
                                
                                targetHeight = report.boundingRectHeight;
                            } 
//                            System.out.println("particle: " + i + " is a Horizontal Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                            horizontalTargets[horizontalTargetCount++] = particleNum; //Add particle to target array and increment count
                        } 
//                        // if not, check if it's a vertical target. If so, announce it.
//                        else if (scoreCompare(scores[i], true)) {
////                            System.out.println("particle: " + i + " is a Vertical Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
//                            verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
//                        } 
//                        else { // it is not a target. Announce this.
////                            System.out.println("particle: " + i + " is not a Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
//                        }
//                        // print out the scores so the programmer can see them and not just the verdict.
////                        System.out.println("rectangularity score: " + scores[i].rectangularity + "ARHoriz score: " + scores[i].aspectRatioHorizontal + "ARVert score: " + scores[i].aspectRatioVertical);                         
//                    }
//                    // It has by now evaluated every particle for being a target.
//
//                    // the "target" variable has many properties like "Scores" does for grading a particle
//                    target.totalScore = target.leftScore = target.rightScore = target.tapeWidthScore = target.verticalScore = 0; // reset all values to 0
//                    // verticalIndex is the particle number which is most likely the target
//                    target.verticalIndex = verticalTargets[0]; // verticalTargets might or might not be empty. If through the grading process a better target is found the index will be changed. 
//                    for (int i = 0; i < verticalTargetCount; i++){ // goes through each vertical target found by the previous for loop.
//                        ParticleAnalysisReport verticalReport = filteredImage.getParticleAnalysisReport(verticalTargets[i]); // just like before, gets the properties of the particle, only now we know it is likely a target
//                        
//                        for (int j = 0; j < horizontalTargetCount; j++){ // now tries matching a horizontal target to the vertical target. It might it might not there depending on if the goal is hot.
//                            ParticleAnalysisReport horizontalReport = filteredImage.getParticleAnalysisReport(horizontalTargets[j]); // just like before, gets the properties of the particle, only now we know it is likely a target
//                            double horizWidth, horizHeight, vertWidth, leftScore, rightScore, tapeWidthScore, verticalScore, total;
//
//                            //Measure sides of rectangles
//                            horizWidth = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
//                            horizHeight = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
//                            vertWidth = NIVision.MeasureParticle(filteredImage.image, verticalTargets[i], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
//
//                            //Determine if the horizontal target is in the expected location to the left of the vertical target
//                            leftScore = ratioToScore(1.2*(verticalReport.boundingRectLeft - horizontalReport.center_mass_x)/horizWidth);
//                            //Determine if the horizontal target is in the expected location to the right of the  vertical target
//                            rightScore = ratioToScore(1.2*(horizontalReport.center_mass_x - verticalReport.boundingRectLeft - verticalReport.boundingRectWidth)/horizWidth);
//                            //Determine if the width of the tape on the two targets appears to be the same
//                            tapeWidthScore = ratioToScore(vertWidth/horizHeight); // must use these because the horizWidth and vertHieght are not the same
//                            //Determine if the vertical location of the horizontal target appears to be correct
//                            verticalScore = ratioToScore(1-(verticalReport.boundingRectTop - horizontalReport.center_mass_y)/(4*horizHeight)); // should be (1-.042)/.999, about as close to 1 as we will get
//                            total = leftScore > rightScore ? leftScore:rightScore; // condition ? value_if_true : value_if_false. Chooses the higher Score
//                            total += tapeWidthScore + verticalScore; // we want a high total.
//
//                            if(total > target.totalScore) { //If best target so far, store the information about it
//                                target.horizontalIndex = horizontalTargets[j];
//                                target.verticalIndex = verticalTargets[i];
//                                target.totalScore = total;
//                                target.leftScore = leftScore;
//                                target.rightScore = rightScore;
//                                target.tapeWidthScore = tapeWidthScore;
//                                target.verticalScore = verticalScore;
//                            }
//                        }       
//                        target.Hot = hotOrNot(target); //Determine if the best target is a Hot target
                    }
                    if(horizontalTargetCount > 0){
                        Xcoord = bestSoFar;        
                        distance = computeDistance(targetHeight);
                        SmartDashboard.putNumber("CameraDistance", distance);
                        SmartDashboard.putString("", "Horizontal Target centerX: " + Xcoord);
                        SmartDashboard.putNumber("Height", targetHeight);                        
                    }
                    else {
                        Xcoord = -1;
                        SmartDashboard.putString("", "No Horizontal Target");
                    }
//                    if(verticalTargetCount > 0){
//                        //Information about the target is contained in the "target" structure
//                        //To get measurement information such as sizes or locations use the
//                        //horizontal or vertical index to get the particle report as shown below
//                        ParticleAnalysisReport distanceReport = filteredImage.getParticleAnalysisReport(target.verticalIndex);
//                        // calculates distance to target
//                        double distance = computeDistance(filteredImage, distanceReport, target.verticalIndex);
//                        if(target.Hot){
//                            System.out.println("Hot target located");
//                            System.out.println("Distance: " + distance);
//                        } 
//                        else {
//                            System.out.println("No hot target present");
//                            System.out.println("Distance: " + distance);
//                        }
//                    }
                }                
                // Not calling free() will cause the memory to accumulate over each pass of this loop.                 
                filteredImage.free();
                thresholdImage.free();
                image.free();
            }    
            catch (AxisCameraException ex){        // this is needed if the camera.getImage() is called
                ex.printStackTrace();
            } 
            catch (NIVisionException ex) {
                ex.printStackTrace();
            }
        }
//    }
    public class Scores {
        double rectangularity;
        double aspectRatioVertical;
        double aspectRatioHorizontal;
    }
    
    public class TargetReport {
        int verticalIndex;
        int horizontalIndex;
        int centerX;
        int centerY;
        boolean Hot;
        double totalScore;
        double leftScore;
        double rightScore;
        double tapeWidthScore;
        double verticalScore;        
    }
    /**
     * Computes the estimated distance to a target using the width of the horizontal target particle in the image. 
     * this assumes though that the camera is looking at the goal perpendicularly, which is not perfectly true. distance is thus an estimation, not exact
     */
    double computeDistance (double height) throws NIVisionException {
//    double computeDistance (BinaryImage image, ParticleAnalysisReport report, int particleNumber) throws NIVisionException {
//        double rectLong, width, height;
//        int targetHeight;
//
//        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
//        //using the smaller of the estimated target's long side and the bounding rectangle width results in better performance on skewed rectangles
//        width = Math.min(report.boundingRectWidth, rectLong);
//        height = Math.min(report.boundingRectHeight, rectLong);
        // TargetInInches/TargetInPixels = FieldOfViewInInches/FieldOfViewInPixels
        // FieldOfViewInInches = 2*dist from center of target to edge of FOV = 2*distance to wall*tan (angle between side of wall and center of target to camera)
        // so dist in feet to wall = FieldOfViewInPixels*(TargetInInches)/(2*TargetInPixels*tan angle*12inchesPerFoot)
        
        return (-2.0*height)+28.0; // dist = -2Height + 28
//        Y_IMAGE_RES * (targetAbsoluteWidth) / (2*width * Math.tan((VIEW_ANGLE*Math.PI/180)/2)*12); //angle in degrees * (pi/180) = radians for Math.tan function
    }    
//    ORIGINAL using vert target: double computeDistance (BinaryImage image, ParticleAnalysisReport report, int particleNumber) throws NIVisionException {
//        double rectLong, height;
//        int targetHeight;
//
//        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
//        //using the smaller of the estimated vertical target's long side and the bounding rectangle height results in better performance on skewed rectangles
//        height = Math.min(report.boundingRectHeight, rectLong);
//        targetHeight = 32;
//        // TargetInInches/TargetInPixels = FieldOfViewInInches/FieldOfViewInPixels
//        // FieldOfViewInInches = 2*dist from center of target to edge of FOV = 2*distance to wall*tan (angle between side of wall and center of target to camera)
//        // so dist in feet to wall = FieldOfViewInPixels*(TargetInInches)/(2*TargetInPixels*tan angle*12inchesPerFoot)
//        return Y_IMAGE_RES * (targetHeight) / (2*height * Math.tan((VIEW_ANGLE*Math.PI/180)/2)*12); //angle in degrees * (pi/180) = radians for Math.tan function
//    }    
    /**
     * Computes a score (0-100) comparing the aspect ratio to the ideal aspect ratio for the target.     
     */
    public double scoreAspectRatio(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean vertical) throws NIVisionException
    {
        double rectLong, rectShort, aspectRatio, aspectRatio2, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        idealAspectRatio = vertical ? (4.0/32) : (23.5/4); // if vertical reflector, use left measurements, if not use right.
	
        //Divide width by height to measure aspect ratio
        aspectRatio2 = vertical ? ratioToScore((rectShort/rectLong)/idealAspectRatio) : ratioToScore((rectLong/rectShort)/idealAspectRatio);
        // above 1 line is the same as below 8
        if(report.boundingRectWidth > report.boundingRectHeight){
            //particle is wider than it is tall, divide long by short
            aspectRatio = ratioToScore((rectLong/rectShort)/idealAspectRatio);
        } else {
            //particle is taller than it is wide, divide short by long
            aspectRatio = ratioToScore((rectShort/rectLong)/idealAspectRatio);
        }
	return aspectRatio;
    }
    boolean scoreCompare(Scores scores, boolean vertical){
	boolean isTarget = true;
//        SmartDashboard.putNumber("rectangularity", scores.rectangularity);
	isTarget &= scores.rectangularity > RECTANGULARITY_LIMIT; // if not a rectangle, then not a target. orientation unknown.
//        SmartDashboard.putBoolean("rectangular test", isTarget);
//        SmartDashboard.putNumber("AR Ratio", scores.aspectRatioHorizontal);
        if(vertical){
            isTarget &= scores.aspectRatioVertical > ASPECT_RATIO_LIMIT; // if its supposed to be verticle rectangle, does it have correct side proportions
        } 
        else {
            isTarget &= scores.aspectRatioHorizontal > ASPECT_RATIO_LIMIT;// if its supposed to be horizontal rectangle, does it have correct side proportions
	}
//        SmartDashboard.putBoolean("AR test", isTarget);
        return isTarget; // if either not a rectangle or the side ratios are incorrect, not a target. Otherwise it is.
    }
    double scoreRectangularity(ParticleAnalysisReport report){
        if(report.boundingRectWidth*report.boundingRectHeight !=0){ // if it has area then continue. It can't be 0 or the calculation below will error
            return 100*report.particleArea/(report.boundingRectWidth*report.boundingRectHeight);
        } 
        else {
            return 0;
        }	
    }
    // linear going from (0,0) to (1,100) to (2,0) and is 0 for all inputs outside the range 0-2     
    double ratioToScore(double ratio){
        return (Math.max(0, Math.min(100*(1-Math.abs(1-ratio)), 100)));
    }
    boolean hotOrNot(TargetReport target){
        boolean isHot = true;

        isHot &= target.tapeWidthScore >= TAPE_WIDTH_LIMIT; // if the score is ever lower, then it is an invalid target
        isHot &= target.verticalScore >= VERTICAL_SCORE_LIMIT;
        isHot &= (target.leftScore > LR_SCORE_LIMIT) | (target.rightScore > LR_SCORE_LIMIT);// if either the left or right score are within the allowable range it is hot.

        return isHot;
    }
}

