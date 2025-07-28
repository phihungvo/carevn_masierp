import { IEmployeeProfiles } from "app/shared/model/employee.model";

export const filterDataEmployee = (listEmployee: IEmployeeProfiles[], employeeId: string[]): IEmployeeProfiles[] => {
    return listEmployee?.filter((employee) => employeeId?.includes(employee.id));
}