package org.bonitasoft.engine.embedded.bpm.setup;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.DemuxOutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

public class BonitaPlatformSetupTool {
	public static int launch(String className, String setupPath) {
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

		try {
			Java task = new Java();
			task.setProject(project);
			task.setNewenvironment(true);
			task.setTaskName("BonitaPlatformSetupTool");
			task.setFork(true);
			task.setFailonerror(true);
			task.setClassname(className);

			// Replicate the current classpath
			URL[] urls = ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs();
			Path classPath = new Path(project);
			for(URL url : urls) {
				FileSet fileSet = new FileSet();
				fileSet.setFile(new File(url.getFile()));
				classPath.addFileset(fileSet);
			}
			task.setClasspath(classPath);
			
			task.setArgs("default \"" + setupPath + "\"");

			// Initialize and execute the task
			task.init();
			return task.executeJava();
		} catch (BuildException e) {
			project.fireBuildFinished(e);
			e.printStackTrace();
			return -1;
		} finally {
			project.log("Ant run finished");
		}
	}
}
