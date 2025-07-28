export const checkHighlightDeadline = (deadline: string) => {
  const currentDate = new Date().setHours(0, 0, 0, 0);
    const deadlineDate = new Date(deadline).setHours(0, 0, 0, 0);
    if (deadlineDate < currentDate) return true;
    return false;
}
