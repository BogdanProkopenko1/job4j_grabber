package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.html.SqlRuParse;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {

    private final Properties cfg = new Properties();

    public Store store() {
        return new PsqlStore(cfg);
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg() throws IOException {
        try (InputStream in = new FileInputStream(new File(
                "C:\\projects\\job4j_grabber\\src\\main\\resources\\app.properties"
        ))) {
            cfg.load(in);
        }
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInHours(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            try {
                parse.list("https://www.sql.ru/forum/job-offers").forEach(store::save);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server =
                         new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(socket.getInputStream()))
                    ) {
                        String str = reader.readLine();
                        while (!(str).isEmpty()) {
                            System.out.println(str);
                            if (str.startsWith("GET /?")) {
                                String[] arguments = str.split("[= ]");
                                out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                                if (arguments[2].equals("GetAll")) {
                                    for (Post post : store.getAll()) {
                                        out.write(post.toString().getBytes(Charset.forName("Windows-1251")));
                                    }
                                    socket.close();
                                } else if (arguments[2].equals("Find")) {
                                    out.write(store.findById(arguments[3]).toString()
                                            .getBytes(Charset.forName("Windows-1251")));
                                    socket.close();
                                }
                            }
                            str = reader.readLine();
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
        grab.web(store);
    }
}