import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import { useState } from 'react';
import useEmployee from './use-employee';

const { useGetListProfileByIds } = useEmployee;

type ResponseType = {
  handleEmployeeChange: (ids: string[]) => void,
  employeesObj: Record<string, IEmployeeProfiles> | undefined
}

const useEmployeesObj = (): ResponseType => {
  const [employmentIds, setEmployeeIds] = useState<string[]>([])
  const employees_query = useGetListProfileByIds(
    employmentIds,
    (data) => data.reduce((acum, item) => ({ ...acum, [item?.id]: item }), {})
  )

  const handleEmployeeChange = (ids: string[]) => setEmployeeIds(ids)

  return {
    handleEmployeeChange,
    employeesObj: employees_query?.data as Record<string, IEmployeeProfiles> | undefined
  }
}

export default useEmployeesObj
