/*
package com.voting.system.votingservice.service;

import com.voting.system.votingservice.model.Candidate;
import com.voting.system.votingservice.model.Polling;
import com.voting.system.votingservice.model.User;
import com.voting.system.votingservice.model.Voting;
import com.voting.system.votingservice.repository.CandidateRepository;
import com.voting.system.votingservice.repository.PollingRepository;
import com.voting.system.votingservice.repository.UserRepository;
import com.voting.system.votingservice.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final VotingRepository votingRepository;
    private final PollingRepository pollingRepository;

    public void castVote(String userCNIC, String candidateCNIC) {

        LocalDateTime realDateTime = LocalDateTime.now();

        Optional<Polling> startDateAndTimeOptional = pollingRepository.findFirstByOrderByStartDateAndTimeAsc();
        Optional<Polling> endDateAndTimeOptional = pollingRepository.findFirstByOrderByEndDateAndTimeDesc();

        if (startDateAndTimeOptional.isPresent() && endDateAndTimeOptional.isPresent()){
            Polling startDateAndTime = startDateAndTimeOptional.get();
            Polling endDateAndTime = endDateAndTimeOptional.get();

            if (!realDateTime.isBefore(startDateAndTime.getStartDateAndTime())) {
                if (!realDateTime.isAfter(endDateAndTime.getEndDateAndTime())) {

                    if (!userHasVoted(userCNIC)) {
                            Optional<User> userOptional = userRepository.findByUserCNIC(userCNIC);
                            Optional<Candidate> candidateOptional = candidateRepository.findByUser_UserCNIC(candidateCNIC.trim());

                            if (userOptional.isPresent() && candidateOptional.isPresent()) {
                                User user = userOptional.get();
                                Candidate candidate = candidateOptional.get();

                                if (user.getUserConstituency().equals(candidate.getUser().getUserConstituency())) {
                                    Voting voting = new Voting();
                                    voting.setUser(user);
                                    voting.setCandidate(candidate);
                                    votingRepository.save(voting);
                                } else {
                                    log.error("User and candidate belong to different constituencies");
                                    throw new IllegalStateException("User and candidate belong to different constituencies");
                                }
                            } else {
                                log.error("User or candidate not found");
                                throw new IllegalArgumentException("User or candidate not found");
                            }
                        } else {
                            log.error("User has already voted");
                            throw new IllegalStateException("User has already voted");
                        }
                }
                else {
                    log.error("Polling has ended. You cannot cast your vote anymore.");
                    throw new IllegalStateException("Polling has ended. You cannot cast your vote anymore.");
                }
            }
            else {
                log.error("Polling has not started yet");
                throw new IllegalStateException("Polling has not started yet");
            }
        }
        else {
            log.error("Start or end date and time not found");
            throw new IllegalStateException("Start or end date and time not found");
        }
    }

    public void wonCandidate(String userCNIC){

        LocalDateTime realDateTime = LocalDateTime.now();

        Optional<Polling> startDateAndTimeOptional = pollingRepository.findFirstByOrderByStartDateAndTimeAsc();
        Optional<Polling> endDateAndTimeOptional = pollingRepository.findFirstByOrderByEndDateAndTimeDesc();

        if (startDateAndTimeOptional.isPresent() && endDateAndTimeOptional.isPresent()){
            Polling startDateAndTime = startDateAndTimeOptional.get();
            Polling endDateAndTime = endDateAndTimeOptional.get();

            if (!realDateTime.isBefore(startDateAndTime.getStartDateAndTime())) {
                if (!realDateTime.isAfter(endDateAndTime.getEndDateAndTime())) {

                        Optional<User> userOptional = userRepository.findByUserCNIC(userCNIC);
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            String userConstituency = user.getUserConstituency();
                            //I want to calculate the votes each candidate has received in each Constituency
                            //then return/show the highest vote gainer and announce me winner

                        } else {
                            log.error("User not found");
                            throw new IllegalArgumentException("User not found");
                        }
                }
                else {
                    log.error("Polling has ended. You cannot cast your vote anymore.");
                    throw new IllegalStateException("Polling has ended. You cannot cast your vote anymore.");
                }
            }
            else {
                log.error("Polling has not started yet");
                throw new IllegalStateException("Polling has not started yet");
            }
        }
        else {
            log.error("Start or end date and time not found");
            throw new IllegalStateException("Start or end date and time not found");
        }
    }

    private boolean userHasVoted(String userCNIC) {
        List<Voting> userVotes = votingRepository.findByUser_UserCNIC(userCNIC);
        return !userVotes.isEmpty();
    }

}








*/
/*
package com.voting.system.votingservice.service;

import com.voting.system.votingservice.model.Candidate;
import com.voting.system.votingservice.model.Polling;
import com.voting.system.votingservice.model.User;
import com.voting.system.votingservice.model.Voting;
import com.voting.system.votingservice.repository.CandidateRepository;
import com.voting.system.votingservice.repository.PollingRepository;
import com.voting.system.votingservice.repository.UserRepository;
import com.voting.system.votingservice.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final VotingRepository votingRepository;
    private final PollingRepository pollingRepository;
  public boolean castVote(String userCNIC, String candidateCNIC) {
      try {
          LocalDateTime localDateTime = LocalDateTime.now();

          List<Polling> pollingEntries = pollingRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(localDateTime.truncatedTo(ChronoUnit.SECONDS),
                  localDateTime.truncatedTo(ChronoUnit.SECONDS));

          if (!pollingEntries.isEmpty()) {
              Polling pollingEntry = pollingEntries.get(0);

              LocalDateTime startDate = pollingEntry.getStartDateTime();
              LocalDateTime endDate = pollingEntry.getEndDateTime();

              if (startDate.isBefore(localDateTime) && endDate.isAfter(localDateTime)) {
                  System.out.println("Polling ID: " + pollingEntry.getPollingId());

                  if (!userHasVoted(userCNIC, startDateAndTime)) {
                          Optional<User> userOptional = userRepository.findByUserCNIC(userCNIC);
                          Optional<Candidate> candidateOptional = candidateRepository.findByUser_UserCNIC(candidateCNIC.trim());

                          if (userOptional.isPresent()) {
                              User user = userOptional.get();

                              if(candidateOptional.isPresent()){
                                  Candidate candidate = candidateOptional.get();

                                  if (user.getUserConstituency().equals(candidate.getUser().getUserConstituency())) {
                                      Voting voting = new Voting();
                                      voting.setUser(user);
                                      voting.setCandidate(candidate);
                                      voting.setPolling(startDateAndTime);
                                      votingRepository.save(voting);
                                      return true;
                                  } else {
                                      log.error("User and candidate belong to different constituencies");
                                      throw new IllegalStateException("User and candidate belong to different constituencies");

                                  }
                              } else {
                                  log.error("Candidate not found");
                                  throw new IllegalArgumentException("Candidate not found");

                              }
                          } else {
                              log.error("User not found");
                              throw new IllegalArgumentException("User not found");

                          }
                      } else {
                          log.error("User has already voted");
                          throw new IllegalStateException("User has already voted");

                      }
                  }
                  else {
                      log.error("Polling has ended. You cannot cast your vote anymore.");
                      throw new IllegalStateException("Polling has ended. You cannot cast your vote anymore.");

                  }
              }
              else {
                  log.error("Polling has not started yet");
                  throw new IllegalStateException("Polling has not started yet");

              }

          }
      } catch (IllegalStateException | IllegalArgumentException e) {
          log.error("Failed to cast vote: {}", e.getMessage());
          return false;
      }
              } else {
                  System.out.println("No active polling found for the current date and time.");
              }
          } else {
              System.out.println("No polling entry found for the current date and time.");
          }



      }catch (Exception e) {
          log.error("An unexpected error occurred while casting vote", e);
          return false;
      }

      return false;
  }

    private boolean userHasVoted(String userCNIC, Polling polling) {
        List<Voting> userVotes = votingRepository.findByUser_UserCNICAndPolling(userCNIC, polling);
        return !userVotes.isEmpty();
    }



    */
/*public void calculateWinner(String userCNIC) {
        LocalDateTime realDateTime = LocalDateTime.now();
        Optional<Polling> electionDate = getElectionDate();

        if (electionDate.isPresent()) {
            Polling dateAndTime = electionDate.get();
             if (isPollingActive(dateAndTime, realDateTime)) {
            List<Candidate> candidates = candidateRepository.findAll();
            Map<String, Map<String, Integer>> constituencyVotes = new HashMap<>();

            for (Candidate candidate : candidates) {
                long candidateId = candidate.getCandidateID();
                int votes = votingRepository.countByCandidateCandidateID(candidateId);

                String constituency = candidate.getUser().getUserConstituency();
                Map<String, Integer> votesMap = constituencyVotes.computeIfAbsent(constituency, k -> new HashMap<>());
                votesMap.put(candidate.getCandidatePartyName(), votes);
            }

            Optional<User> user = userRepository.findByUserCNIC(userCNIC);
            if (user.isPresent()) {
                String userConstituency = user.get().getUserConstituency();

                Map<String, Integer> userConstituencyVotes = constituencyVotes.get(userConstituency);
                if (userConstituencyVotes != null && !userConstituencyVotes.isEmpty()) {
                    String winnerName = "";
                    int maxVotes = 0;

                    for (Map.Entry<String, Integer> candidateEntry : userConstituencyVotes.entrySet()) {
                        String candidateName = candidateEntry.getKey();
                        int votes = candidateEntry.getValue();

                        if (votes > maxVotes) {
                            winnerName = candidateName;
                            maxVotes = votes;
                        }
                    }

                    log.info("Winner in constituency {} is candidate {} with {} votes", userConstituency, winnerName, maxVotes);
                } else {
                    log.info("No winner found for constituency {}", userConstituency);
                }
            } else {
                log.error("User not found");
                throw new IllegalArgumentException("User not found");
            }
        } else {
            log.error("Polling has not started yet or has ended");
            throw new IllegalStateException("Polling has not started yet or has ended");
        }
        } else {
            log.error("Election date and time not found");
            throw new IllegalStateException("Election date and time not found");
        }
    }*//*}*/

package com.voting.system.votingservice.service;

import com.voting.system.votingservice.model.*;
import com.voting.system.votingservice.repository.CandidateRepository;
import com.voting.system.votingservice.repository.PollingRepository;
import com.voting.system.votingservice.repository.UserRepository;
import com.voting.system.votingservice.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final VotingRepository votingRepository;
    private final PollingRepository pollingRepository;
    private final AuthenticationManager authenticationManager;

    /*public ResponseEntity<String> castVote(String userCNIC, String candidateCNIC) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();

            List<Polling> pollingEntries = pollingRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(
                    localDateTime.truncatedTo(ChronoUnit.SECONDS),
                    localDateTime.truncatedTo(ChronoUnit.SECONDS));

            if (!pollingEntries.isEmpty()) {
                Polling pollingEntry = pollingEntries.get(0);

                LocalDateTime startDate = pollingEntry.getStartDateTime();
                LocalDateTime endDate = pollingEntry.getEndDateTime();

                if (startDate.isBefore(localDateTime) && endDate.isAfter(localDateTime)) {
                    System.out.println("Polling ID: " + pollingEntry.getPollingId());

                    if (!userHasVoted(userCNIC, pollingEntry)) {
                        Optional<User> userOptional = userRepository.findByUserCNIC(userCNIC);
                        Optional<Candidate> candidateOptional = candidateRepository.findByUser_UserCNIC(candidateCNIC.trim());

                        if (userOptional.isPresent()) {
                            User user = userOptional.get();

                            if (candidateOptional.isPresent()) {
                                Candidate candidate = candidateOptional.get();

                                if (user.getUserConstituency().equals(candidate.getUser().getUserConstituency())) {
                                    Voting voting = new Voting();
                                    voting.setUser(user);
                                    voting.setCandidate(candidate);
                                    voting.setPolling(pollingEntry);
                                    votingRepository.save(voting);
                                    return ResponseEntity.ok("Vote cast successfully.");
                                } else {
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User and candidate belong to different constituencies");
                                }
                            } else {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Candidate not found");
                            }
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User has already voted");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Polling has ended. You cannot cast your vote anymore.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active polling found for the current date and time.");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cast vote: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while casting vote");
        }
    }*/

    public ResponseEntity<String> castVote(String userCNIC, String candidateCNIC) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
            }

            Optional<User> userOption = userRepository.findUsernameByUserCNIC(userCNIC);

            if (userOption.isPresent()) {
                User user = userOption.get();

                String loggedInUserCNIC = getUserCNICFromAuthentication(authentication);
                String userName = user.getUserName();
                if (!userName.equals(loggedInUserCNIC)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with userCNIC " + userCNIC + " is not logged in");
                }
            }

            LocalDateTime localDateTime = LocalDateTime.now();

            List<Polling> pollingEntries = pollingRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(
                    localDateTime.truncatedTo(ChronoUnit.SECONDS),
                    localDateTime.truncatedTo(ChronoUnit.SECONDS));

            // Filter out duplicate entries with the same start and end date times
            pollingEntries = pollingEntries.stream()
                    .distinct()
                    .collect(Collectors.toList());
            System.out.println("*******"  + pollingEntries.size());
            if (!pollingEntries.isEmpty()) {
                for (int i = 0; i <= pollingEntries.size() - 1; i++) {
                    Polling pollingEntry = pollingEntries.get(i);
                    LocalDateTime startDate = pollingEntry.getStartDateTime();
                    LocalDateTime endDate = pollingEntry.getEndDateTime();

                    if (startDate.isBefore(localDateTime) && endDate.isAfter(localDateTime)) {
                        System.out.println("Polling ID: " + pollingEntry.getPollingId());

                        if (!userHasVoted(userCNIC, pollingEntry)) {
                            Optional<User> userOptional = userRepository.findByUserCNIC(userCNIC);
                            Optional<Candidate> candidateOptional = candidateRepository.findByUser_UserCNIC(candidateCNIC.trim());

                            if (userOptional.isPresent()) {
                                User user = userOptional.get();

                                if (candidateOptional.isPresent()) {
                                    Candidate candidate = candidateOptional.get();

                                    if (user.getUserConstituency().equals(candidate.getUser().getUserConstituency())) {
                                        Voting voting = new Voting();
                                        voting.setUser(user);
                                        voting.setCandidate(candidate);
                                        voting.setPolling(pollingEntry);
                                        votingRepository.save(voting);
                                        return ResponseEntity.ok("Vote cast successfully.");
                                    } else {
                                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User and candidate belong to different constituencies");
                                    }
                                } else {
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Candidate not found");
                                }
                            } else {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
                            }
                        }
                    }
                }
                return ResponseEntity.ok("Vote cast successfully in all polling entries.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active polling found for the current date and time.");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cast vote: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while casting vote");
        }
    }

    private String getUserCNICFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }


    public ResponseEntity<String> calculateWinner(String userCNIC, long pollingId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Optional<Polling> pollingOptional = pollingRepository.findById(pollingId);

        if (pollingOptional.isPresent()) {
            Polling polling = pollingOptional.get();

            if (polling.getEndDateTime().isBefore(currentDateTime)) {
                log.info("Calculating winner for Polling ID: {}", polling.getPollingId());

                List<Voting> votingList = polling.getVotings();
                Map<String, Map<String, Integer>> constituencyVotes = new HashMap<>();

                Optional<User> userOptional = userRepository.findByUserCNIC(userCNIC);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    String userConstituency = user.getUserConstituency();

                    /*for (Voting voting : votingList) {
                        Candidate candidate = voting.getCandidate();
                        User candidateUser = candidate.getUser();

                        if (candidateUser != null && userConstituency.equals(candidateUser.getUserConstituency())) {
                            String candidateConstituency = candidateUser.getUserConstituency();

                            constituencyVotes.putIfAbsent(candidateConstituency, new HashMap<>());
                            Map<String, Integer> candidateVotes = constituencyVotes.get(candidateConstituency);
                            candidateVotes.put(candidate.getCandidatePartyName(), candidateVotes.getOrDefault(candidate.getCandidatePartyName(), 0) + 1);
                        }
                    }*/

                    votingList.stream()
                            .map(Voting::getCandidate)
                            .filter(candidate -> {
                                User candidateUser = candidate.getUser();
                                return candidateUser != null && userConstituency.equals(candidateUser.getUserConstituency());
                            })
                            .forEach(candidate -> {
                                User candidateUser = candidate.getUser();
                                String candidateConstituency = candidateUser.getUserConstituency();
                                constituencyVotes.putIfAbsent(candidateConstituency, new HashMap<>());
                                Map<String, Integer> candidateVotes = constituencyVotes.get(candidateConstituency);
                                candidateVotes.put(candidate.getCandidatePartyName(), candidateVotes.getOrDefault(candidate.getCandidatePartyName(), 0) + 1);
                            });


                    Map<String, Integer> userConstituencyVotes = constituencyVotes.get(userConstituency);
                    String winnerName = userConstituencyVotes.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse(null);

                    int maxVotes = userConstituencyVotes.getOrDefault(winnerName, 0);

                    if (winnerName != null) {
                        return ResponseEntity.ok("Winner in constituency " + userConstituency + " for Polling ID " + pollingId + " is candidate " + winnerName + " with " + maxVotes + " votes");
                    } else if (!userConstituencyVotes.isEmpty()) {
                        return ResponseEntity.ok("No winner found for constituency " + userConstituency + " and Polling ID " + pollingId);
                    } else {
                        return ResponseEntity.ok("No votes found for constituency " + userConstituency + " in Polling ID " + pollingId);
                    }

                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with CNIC " + userCNIC + " not found.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Polling with ID " + pollingId + " is still ongoing. Cannot calculate winner until polling ends.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Polling with ID " + pollingId + " not found.");

        }
    }



    public ResponseEntity<String> calculateWinnerInAllConstituency(long pollingId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Optional<Polling> pollingOptional = pollingRepository.findById(pollingId);

        if (pollingOptional.isPresent()) {
            Polling polling = pollingOptional.get();

            if (polling.getEndDateTime().isBefore(currentDateTime)) {
                log.info("Calculating winner for Polling ID: {}", polling.getPollingId());

                List<Voting> votingList = polling.getVotings();
                Map<String, Map<String, Integer>> constituencyVotes = new HashMap<>();

                for (Voting voting : votingList) {
                    Candidate candidate = voting.getCandidate();
                    User candidateUser = candidate.getUser();
                    String candidateConstituency = candidateUser.getUserConstituency();
                    String candidatePartyName = candidate.getCandidatePartyName();

                    constituencyVotes.putIfAbsent(candidateConstituency, new HashMap<>());
                    Map<String, Integer> candidateVotes = constituencyVotes.get(candidateConstituency);
                    candidateVotes.put(candidatePartyName, candidateVotes.getOrDefault(candidatePartyName, 0) + 1);
                }

                StringBuilder result = new StringBuilder();
                for (Map.Entry<String, Map<String, Integer>> entry : constituencyVotes.entrySet()) {
                    String constituency = entry.getKey();
                    Map<String, Integer> candidateVotes = entry.getValue();

                    String winnerName = candidateVotes.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse(null);

                    int maxVotes = candidateVotes.getOrDefault(winnerName, 0);

                    if (winnerName != null) {
                        result.append("Winner in constituency ").append(constituency)
                                .append(" for Polling ID ").append(pollingId)
                                .append(" is candidate ").append(winnerName)
                                .append(" with ").append(maxVotes).append(" votes.\n");
                    } else {
                        result.append("No winner found for constituency ").append(constituency)
                                .append(" and Polling ID ").append(pollingId).append(".\n");
                    }
                }

                return ResponseEntity.ok(result.toString());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Polling with ID " + pollingId + " is still ongoing. Cannot calculate winner until polling ends.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Polling with ID " + pollingId + " not found.");
        }
    }



    public ResponseEntity<String> checkVotes(String candidateCNIC, long pollingId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Optional<Polling> pollingOptional = pollingRepository.findById(pollingId);

        if (pollingOptional.isPresent()) {
            Polling polling = pollingOptional.get();

            if (polling.getEndDateTime().isBefore(currentDateTime)) {
                log.info("Fetching votes for candidate with CNIC {} in Polling ID: {}", candidateCNIC, polling.getPollingId());

                List<Voting> votingList = polling.getVotings();
                int candidateVotesCount = 0;

                for (Voting voting : votingList) {
                    Candidate candidate = voting.getCandidate();
                    if (candidate != null && candidate.getUser() != null && candidate.getUser().getUserCNIC().equals(candidateCNIC)) {
                        candidateVotesCount++;
                    }
                }

                return ResponseEntity.ok("Candidate with CNIC " + candidateCNIC + " received " + candidateVotesCount + " votes in Polling ID " + pollingId);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Polling with ID " + pollingId + " is still ongoing. Cannot fetch votes until polling ends.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Polling with ID " + pollingId + " not found.");

        }
    }


    private boolean userHasVoted(String userCNIC, Polling polling) {
        List<Voting> userVotes = votingRepository.findByUser_UserCNICAndPolling(userCNIC, polling);
        return !userVotes.isEmpty();
    }


    public ResponseEntity<String> loginUser(LoginDto loginDto) {
        String username = loginDto.getUserName();
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(username + " doesn't exists");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                            loginDto.getUserPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.status(HttpStatus.OK).body(username + " logged in successfully...");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body("UserName & Password doesn't match");
        }
    }
}
