package org.example.charityapp.DTO;

public record CharityActionResponse(
        Long id,
        String title,
        String description,
        Long organizationId,
        String organizationName,
        Double goalAmount,
        Double currentAmount,
        String status
) {
    // Calculate progress percentage (0-100)
    public int getProgressPercentage() {
        if (goalAmount == null || goalAmount <= 0) return 0;
        double percentage = (currentAmount / goalAmount) * 100;
        return (int) Math.min(100, Math.max(0, percentage)); // Ensure between 0-100
    }
}