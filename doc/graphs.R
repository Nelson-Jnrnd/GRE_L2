setwd('D:/prog/heigVD_2021/GRE/GRE_L2')
algo_perfs = read.csv("output/result-2022-05-16 21-03-33.csv", sep=';')

summary(algo_perfs)

# Times
times <- algo_perfs[, c('Dijkstra.time', 'Bidirectional.Dijkstra.time')]
times <- setNames(times, c("Dijkstra", "Bidirectional Dijkstra"))
boxplot(times, outline = TRUE)
title("Comparison of the execution time", xlab = "Algorithm", ylab = "Execution time in ms")

boxplot(times, outline = FALSE)
title("Comparison of the execution time", xlab = "Algorithm", ylab = "Execution time in ms")


# Iterations
times <- algo_perfs[, c('Dijkstra.iteration', 'Bidirectional.Dijkstra.iteration')]
times <- setNames(times, c("Dijkstra", "Bidirectional Dijkstra"))
boxplot(times, outline = TRUE)
title("Comparison of the number of iterations", xlab = "Algorithm", ylab = "Number of iterations")
boxplot(times, outline = FALSE)
title("Comparison of the number of iterations", xlab = "Algorithm", ylab = "Number of iterations")

# Differences
df <- data.frame(algo_perfs$Dijkstra.time, algo_perfs$Dijkstra.iteration, algo_perfs$Dijkstra.path.length, "Dijkstra")
df <- setNames(df, c("time", "iteration", "path_length", "algorithm"))

df2 <- data.frame(algo_perfs$Bidirectional.Dijkstra.time, algo_perfs$Bidirectional.Dijkstra.iteration, algo_perfs$Bidirectional.Dijkstra.path.length, "Bidirectional Dijkstra")
df2 <- setNames(df2, c("time", "iteration", "path_length", "algorithm"))

unwrapped_df <- rbind(df, df2)

plot(unwrapped_df$path_length, unwrapped_df$iteration, col=ifelse(unwrapped_df$algorithm=="Dijkstra", "red", "blue"), xlab = "Path length (number of nodes)", ylab = "Number of iterations")
title("Iteration per path length")
legend("bottomright", inset=.05, title="Algorithms",
       c("Dijkstra", "Bidirectional Dijkstra"), fill=c("red", "blue"))

plot(algo_perfs$Dijkstra.path.length, algo_perfs$Dijkstra.iteration - algo_perfs$Bidirectional.Dijkstra.iteration, 
     xlab = "Path length (number of nodes)", ylab = "Number of iterations")
title("Iterations gained with Bidirectional Dijkstra per path length")

unwrapped_df$interval <- cut(unwrapped_df$path_length, breaks = seq(0, 300, by=5))
algo_perfs$interval <- cut(algo_perfs$Dijkstra.path.length, breaks = seq(0, max(algo_perfs$Dijkstra.path.length), by=5))

plot(algo_perfs$interval, algo_perfs$Dijkstra.iteration - algo_perfs$Bidirectional.Dijkstra.iteration,
     xlab = "Path length (number of nodes)", ylab = "Number of iterations")
title("Iterations gained with Bidirectional Dijkstra per path length")

library(dplyr)

# Group by percentages

unwrapped_df %>% group_by(unwrapped_df$interval) %>% summarise(n=n())
plot(unwrapped_df %>% group_by(unwrapped_df$interval) %>% summarise(n=n()),
     xlab = "Path length (number of nodes)", ylab = "Number of observations")
title("Number of observations per path length")


unwrapped_df$path_length_percentage <- unwrapped_df$path_length / max(unwrapped_df$path_length) * 100
unwrapped_df$path_length_percentage <- cut(unwrapped_df$path_length_percentage, breaks = seq(0,100, by=1))

dij_df <- unwrapped_df %>% filter(unwrapped_df$algorithm == "Dijkstra") %>% select(path_length_percentage, iteration, time)
bi_dij_df <- unwrapped_df %>% filter(unwrapped_df$algorithm == "Bidirectional Dijkstra") %>% select(path_length_percentage, iteration, time)
grp_by_perc <- dij_df %>% group_by(dij_df$path_length_percentage) %>% summarise_all(mean, na.rm = TRUE)
grp_by_perc2 <- bi_dij_df %>% group_by(bi_dij_df$path_length_percentage) %>% summarise_all(mean, na.rm = TRUE)

plot(1, type="n", xlab="", ylab="", xlim=c(0, 100), ylim=c(0, 30000))
lines(grp_by_perc$`dij_df$path_length_percentage`, grp_by_perc$iteration, type="l", col="red")
lines(grp_by_perc2$`bi_dij_df$path_length_percentage`, grp_by_perc2$iteration, type="l", col="blue")
title("Iteration per path length", xlab = "Path length (% of longest path)", ylab = "Number of iterations")
legend("bottomright", inset=.05, title="Algorithms",
       c("Dijkstra", "Bidirectional Dijkstra"), fill=c("red", "blue"))

plot(1, type="n", xlab="", ylab="", xlim=c(0, 100), ylim=c(0, 50))
lines(grp_by_perc$`dij_df$path_length_percentage`, grp_by_perc$time, type="l", col="red")
lines(grp_by_perc2$`bi_dij_df$path_length_percentage`, grp_by_perc2$time, type="l", col="blue")
title("Execution time per path length", xlab = "Path length (% of longest path)", ylab = "Execution time in ms")
legend("bottomright", inset=.05, title="Algorithms",
       c("Dijkstra", "Bidirectional Dijkstra"), fill=c("red", "blue"))


plot(grp_by_perc$`dij_df$path_length_percentage`, grp_by_perc$time - grp_by_perc2$time,
     xlab = "Path length (% of longest path)", ylab = "Execution time in ms")
title("Execution time gain with Bidirectional Dijkstra per path length")
