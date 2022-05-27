package com.cfa.jobs;

import com.cfa.objects.letter.Letter;
import com.cfa.objects.letter.LetterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to launch jobs from an API call
 */
@RestController
@Slf4j
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/v1/jobcontroller")
@RequiredArgsConstructor
public class JobController {
  @Autowired
  private LetterRepository letterRepository;
  private final JobLauncher jobLauncher;
  private final Job simpleJob;
  private final Job messageLetterJob;

  @RequestMapping("/example")
  public void simpleJob(@RequestParam(value = "label") final String label) {
    runJobB(this.simpleJob, label);
  }

  @RequestMapping("/tme2")
  public void messageLetterJob(@RequestParam(value = "message") final String label) {
    runJobB(this.messageLetterJob, label);
  }
  @GetMapping("/getletters")
  public List<Letter> getAll(){
    return letterRepository.findAll();
  }
  @PostMapping("/postLetter/l")
  public Letter save(@RequestBody Letter l) {
    return letterRepository.save(l);
  }
  @GetMapping("/getById/{id}")
  public Letter getById(@PathVariable("id") int id){
    return letterRepository.getOne(id);
  }
  private void runJobB(final Job parJob, final String label) {
    final JobParameters locParamJobParameters = new JobParametersBuilder()
      .addParameter("value", new JobParameter(label))
      .addParameter("time", new JobParameter(System.currentTimeMillis()))
      .toJobParameters();

    try {
      log.info("[Job] running . . .");
      jobLauncher.run(parJob, locParamJobParameters);
    } catch (Exception ex) {
      log.error("[RUN JOB ERROR] : " + ex.getMessage());
    }
  }

}