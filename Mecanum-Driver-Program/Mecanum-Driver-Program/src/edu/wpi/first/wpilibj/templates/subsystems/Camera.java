
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
    int bestSoFar = 0;
    
    final int Y_IMAGE_RES = 240;		//Y Image resolution in pixels, should be 120, 240 or 480
    final double VIEW_ANGLE = 37.4;  //camera angle from center of field of view to edge

    //Score limits used for target identification
    final int RECTANGULARITY_LIMIT = 65;
    final int ASPECT_RATIO_LIMIT = 70;

    //Score limits used for hot target determination
    final int VERTICAL_SCORE_LIMIT = 50;
    final int LR_SCORE_LIMIT = 50;

    //Minimum area of particles to be considered
    final int AREA_MINIMUM = 130;

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
//                filteredImage.wrcite("/CameraPictures/FilterManualResult"+pictures+".bmp");

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
                        //checks  for the best horizontal target so far, but currently does not take vicinity of vertical into account
//                            if ((scores [particleNum].rectangularity+scores [particleNum].aspectRatioHorizontal) > topScore) {
//                                topScore = scores [particleNum].rectangularity+scores [particleNum].aspectRatioHorizontal;                                
//                                bestSoFar = report.center_mass_x;
//                            } 
//                            System.out.println("particle: " + particleNum + " is a Horizontal Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                            horizontalTargets[horizontalTargetCount++] = particleNum; //Add particle to target array and increment count
                        } 
                        // if not, check if it's a vertical target. If so, announce it.
                        else if (scoreCompare(scores[particleNum], true)) {
//                            System.out.println("particle: " + particleNum + " is a Vertical Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                            verticalTargets[verticalTargetCount++] = particleNum;  //Add particle to target array and increment count
                        } 
//                        else { // it is not a target. Announce this.
//                            System.out.println("particle: " + particleNum + " is not a Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
//                        }
                    }
                    // It has by now evaluated every particle for being a target.

                    // the "target" variable has many properties like "Scores" does for grading a particle
                    target.totalScore = target.leftScore = target.rightScore = target.verticalScore = 0; // reset all values to 0
                    // verticalIndex is the particle number which is most likely the target
                    target.verticalIndex = verticalTargets[0]; // If through the grading process a better target is found the index will be changed. 
                    for (int verticalTargetNum = 0; verticalTargetNum < verticalTargetCount; verticalTargetNum++){ // goes through each vertical target found by the previous for loop.
                        ParticleAnalysisReport verticalReport = filteredImage.getParticleAnalysisReport(verticalTargets[verticalTargetNum]); // just like before, gets the properties of the particle, only now we know it is likely a target
                        for (int horizontalTargetNum = 0; horizontalTargetNum < horizontalTargetCount; horizontalTargetNum++){ // now tries matching a horizontal target to the vertical target. It might it might not there depending on if the goal is hot.
                            ParticleAnalysisReport horizontalReport = filteredImage.getParticleAnalysisReport(horizontalTargets[horizontalTargetNum]); // just like before, gets the properties of the particle, only now we know it is likely a target
                            double horizWidth, horizHeight, vertWidth, leftScore, rightScore, verticalScore, total;

                            //Measure sides of rectangles
                            horizWidth = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[horizontalTargetNum], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
                            horizHeight = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[horizontalTargetNum], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
                            vertWidth = NIVision.MeasureParticle(filteredImage.image, verticalTargets[verticalTargetNum], false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);

                            //Determine if the horizontal target is in the expected location to the left of the vertical target
                            leftScore = ratioToScore((verticalReport.center_mass_x - horizontalReport.center_mass_x)/horizWidth); // from center of vertical to center of horizontal on x-axis is the same as the width of the horizontal
                            //Determine if the horizontal target is in the expected location to the right of the  vertical target
                            rightScore = ratioToScore((horizontalReport.center_mass_x - verticalReport.center_mass_x)/horizWidth); // from center of vertical to center of horizontal on x-axis is the same as the width of the horizontal
                            
                            // makes sure top of vertical target is about at the center of the horizontal
                            System.out.println("ratio of y-coord to each other: "+(verticalReport.boundingRectTop/horizontalReport.center_mass_y));                            
                            verticalScore = ratioToScore(vertWidth/horizHeight); 
                            total = leftScore > rightScore ? leftScore:rightScore; // condition ? value_if_true : value_if_false. Chooses the higher Score
                            System.out.println("total: "+total);
                            System.out.println("left: "+leftScore);
                            System.out.println("right: "+rightScore);
                            System.out.println("vertScore: "+verticalScore);
                            total += verticalScore; // we want a high total.
                            System.out.println("total with vert: "+total);


                            if(total > target.totalScore) { //If best target so far, store the information about it
                                target.horizontalIndex = horizontalTargets[horizontalTargetNum];
                                target.verticalIndex = verticalTargets[verticalTargetNum];
                                target.totalScore = total;
                                target.leftScore = leftScore;
                                target.rightScore = rightScore;
                                target.verticalScore = verticalScore;
                            }
                        }       
                        target.Hot = hotOrNot(target); //Determine if the best target is a Hot target
                    }
                    if(horizontalTargetCount > 0){
                        Xcoord = bestSoFar;        
                        SmartDashboard.putString("", "Horizontal Target centerX: " + Xcoord);
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
        double verticalScore;        
    }
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

        isHot &= target.verticalScore >= VERTICAL_SCORE_LIMIT;
        isHot &= (target.leftScore > LR_SCORE_LIMIT) | (target.rightScore > LR_SCORE_LIMIT);// if either the left or right score are within the allowable range it is hot.

        return isHot;
    }
}

