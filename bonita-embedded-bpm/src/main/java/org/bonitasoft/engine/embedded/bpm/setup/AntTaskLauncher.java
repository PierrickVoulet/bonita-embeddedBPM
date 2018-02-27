package org.bonitasoft.engine.embedded.bpm.setup;

import java.io.File;
import java.io.PrintStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.DemuxOutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline.Argument;

public class AntTaskLauncher {
	public static void launch(String className) {
		// global ant project settings
		Project project = new Project();
		project.setBaseDir(new File(System.getProperty("user.dir")));
		project.init();
		DefaultLogger logger = new DefaultLogger();
		project.addBuildListener(logger);
		logger.setOutputPrintStream(System.out);
		logger.setErrorPrintStream(System.err);
		logger.setMessageOutputLevel(Project.MSG_INFO);
		System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
		System.setErr(new PrintStream(new DemuxOutputStream(project, true)));
		project.fireBuildStarted();

		Throwable caught = null;
		try {
			Java javaTask = new Java();
			javaTask.setNewenvironment(true);
			javaTask.setTaskName("runjava");
			javaTask.setProject(project);
			javaTask.setFork(true);
			javaTask.setFailonerror(true);
			javaTask.setClassname(className);

			// add some vm args
			Argument jvmArgs = javaTask.createJvmarg();
			jvmArgs.setLine("-Xms512m -Xmx512m");

			// added some args for to class to launch
			Argument taskArgs = javaTask.createArg();
			taskArgs.setLine("bla path=/tmp/");

			// set the class path
			Path classPath = new Path(project);
			File classDir = new File(System.getProperty("user.dir"), "target");
			javaTask.setClasspath(classPath);
			Path classPath1 = new Path(project);
			classPath1.setPath(classDir.getPath());
			FileSet fileSet = new FileSet();
			fileSet.setDir(classDir);
			fileSet.setIncludes("**/*.jar");
			classPath1.addFileset(fileSet);
			javaTask.setClasspath(classPath1);

			javaTask.init();
			int ret = javaTask.executeJava();
			System.out.println("return code: " + ret);

		} catch (BuildException e) {
			caught = e;
		}
		project.log("finished");
		project.fireBuildFinished(caught);
	}
}
