export type Filter = Partial<{
    page: number,
    size: number,
    'code.contains': string,
    'status.equals': string,
    'employeeId.equals': string
    isDeleteAll?: boolean
}>