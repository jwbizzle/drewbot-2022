// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class AutoDrive extends CommandBase {
  private final DriveSubsystem m_drive;
  private double m_time, m_speed, m_rotation;

  /** Creates a new AutoDrive. */
  public AutoDrive(DriveSubsystem subsystem, double speed, double rotation) {
    m_drive = subsystem;
    m_speed = speed;
    m_rotation = rotation;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Drive using arcade drive
    m_drive.arcadeDrive(m_speed, m_rotation);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}