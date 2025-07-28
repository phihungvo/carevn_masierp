import { UseFormSetValue } from "react-hook-form";

export const handleNumberOnlyInput = (e: React.ChangeEvent<HTMLInputElement>, setValue: UseFormSetValue<any>, name: string) => {
    let value = e.target.value.replace(/\D+/gi, '');
    setValue(name, value)
}