import { IEmployee } from 'app/shared/model/employee.model'
import { useEffect } from 'react'
import useEmployee from './use-employee'

const {
    useGetEmployeeProfileByIdQuery
} = useEmployee

type Props = {
    id?: string,
    autoSetLabel?: (data: IEmployee & { label: string }) => void
}

const useEmployeeLabel = (payload: Props) => {
    const { id, autoSetLabel } = payload
    const query = useGetEmployeeProfileByIdQuery(id)

    useEffect(() => {
        if (query?.data && autoSetLabel) {
            let data = query?.data
            autoSetLabel({ ...data, label: data?.employeeCode + ' - ' + data?.fullName })
        }
    }, [query?.data])

  return { ...query?.data, label: query?.data?.employeeCode + ' - ' + query?.data?.fullName }
}

export default useEmployeeLabel