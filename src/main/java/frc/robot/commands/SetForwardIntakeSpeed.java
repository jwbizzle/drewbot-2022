// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class SetForwardIntakeSpeed extends CommandBase {
  private final IntakeSubsystem m_intake;
  private DoubleSupplier m_rightTrigger;
  private DoubleSupplier m_leftTrigger;

  /** Creates a new IntakeSpeed. */
  public SetForwardIntakeSpeed(IntakeSubsystem subsystem, DoubleSupplier rightTrigger) {
    m_intake = subsystem;
    m_rightTrigger = rightTrigger;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //System.out.println("Intake Right: " + m_rightTrigger.getAsDouble() + " Intake Left: " + m_leftTrigger.getAsDouble());
    m_intake.setForwardSpeed(m_rightTrigger.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}