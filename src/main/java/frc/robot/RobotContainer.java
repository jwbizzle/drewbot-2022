// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj.XboxController.Button;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.commands.SetArmPositionCommand;
// import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.commands.AutoTimeCommandGroup;
import frc.robot.commands.GrandTheftDriveCommand;
import frc.robot.commands.HalveDriveSpeedCommand;
import frc.robot.commands.SetIntakeSpeedCommand;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final IntakeSubsystem m_robotIntake = new IntakeSubsystem();
  private final ArmSubsystem m_robotArm = new ArmSubsystem();

  // The autonomous routines

  // A simple auto routine that drives forward a specified duration in seconds and then stops.
  //private final Command m_simpleAuto = new SetDriveSpeedCommand(m_robotDrive, AutoConstants.kAutoDriveSpeed, AutoConstants.kAutoDriveRotation).withTimeout(AutoConstants.kAutoDriveDuration);
  private final Command m_simpleAuto = new AutoTimeCommandGroup(m_robotDrive, m_robotIntake, m_robotArm);
  
  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  // The opreators's controller
  XboxController m_operatorController = new XboxController(OIConstants.kOperatorControllerPort);
  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // Configure the button bindings
    configureButtonBindings();
       
    // Configure default commands.  Example Arcade Drive command call:
    // new ArcadeDriveCommand(m_robotDrive, m_driverController::getLeftY, m_driverController::getRightX)
    m_robotDrive.setDefaultCommand(
     new GrandTheftDriveCommand(m_robotDrive, m_driverController::getRightTriggerAxis, m_driverController::getLeftTriggerAxis, m_driverController::getLeftX)
     );

    // Add commands to the autonomous command chooser
    m_chooser.setDefaultOption("Auto Shoot and Taxi", m_simpleAuto);

    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // While the driver is holding the shoulder button, drive at half speed
    new JoystickButton(m_driverController, Button.kRightBumper.value)
        .whenHeld(new HalveDriveSpeedCommand(m_robotDrive));

    // Control the intake motor speed while the operator is holder the triggers
    Trigger rightTriggerButton = new Trigger(() -> m_operatorController.getRightTriggerAxis() > OIConstants.kDeadbandThreshold);
    Trigger leftTriggerButton = new Trigger(() -> m_operatorController.getLeftTriggerAxis() > OIConstants.kDeadbandThreshold);

    rightTriggerButton.whileActiveOnce(new SetIntakeSpeedCommand(m_robotIntake, IntakeConstants.kIntakeMotorReverseSpeed));
    leftTriggerButton.whileActiveOnce(new SetIntakeSpeedCommand(m_robotIntake, IntakeConstants.kIntakeMotorForwardSpeed));

    rightTriggerButton.or(leftTriggerButton).whenInactive(new SetIntakeSpeedCommand(m_robotIntake, IntakeConstants.kIntakeMotorStopSpeed));

    //Arm Buttons
    new JoystickButton(m_operatorController, Button.kRightBumper.value).whenPressed(new SetArmPositionCommand(m_robotArm, true));
    new JoystickButton(m_operatorController, Button.kLeftBumper.value).whenPressed(new SetArmPositionCommand(m_robotArm, false));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_chooser.getSelected();
  }
}
