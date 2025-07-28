import React from "react";
import permissionsObject from "./permissionsObject.json";
import {FormGroup, Input, Table} from "reactstrap";
import axios from "axios";

interface PermissionType {
  name: string;
  description: string;
}

const DEFAULT_TOKEN =
  "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NGIzNmU4OS0xNTVlLTRjNGMtOWI2MS04NWNiYzhmNzM1ODkiLCJleHAiOjE3MjM4ODkzMjIsImF1dGgiOiJQRVJNSVNTSU9OLkVNUExPWUVFLkNSRUFURSBQRVJNSVNTSU9OLkVNUExPWUVFLlJFQUQgUk9MRV9BRE1JTiBST0xFX1VTRVIiLCJpYXQiOjE3MjEyOTczMjIsImdyb3VwIjoiM2Q4MzllNTMtMmUwZC00YTQ4LThhZjQtN2EzYzE0ZGQyYjJhIn0.LNwYNZWikCyvLtS0I-bAM_I9YAQzMBL9RbCW0xggCaPXEtpsP-8XuVyNCDcojdxPnank44I-gdzilLVZxxjxeQ";
const ENDPOINT = "/services/masierp/api";
async function fetchMyPermissions(): Promise<string[]> {
  const rest = await axios.get(`${ENDPOINT}/account/authority`, {
    headers: {
      Authorization: `Bearer ${
        localStorage.getItem("jhi-authenticationToken") || DEFAULT_TOKEN
      }`,
      contentType: "application/json",
    },
  });
  return rest.data;
}

async function removePermission(permission: string) {
  await axios.delete(`${ENDPOINT}/my-account/authority`, {
    headers: {
      Authorization: `Bearer ${
        localStorage.getItem("jhi-authenticationToken") || DEFAULT_TOKEN
      }`,
      contentType: "application/json",

    },
    data: {
      authority: permission,
    }
  });
}

async function addPermission(permission: string) {
  await axios.post(`${ENDPOINT}/my-account/authority`, {
    authority: permission,
  }, {
    headers: {
      Authorization: `Bearer ${
        localStorage.getItem("jhi-authenticationToken") || DEFAULT_TOKEN
      }`,
      contentType: "application/json",
    },
  });
}



const permissionsList: PermissionType[] = permissionsObject;
const Permission = () => {
  const [search, setSearch] = React.useState("");
  const [myPermissions, setMyPermissions] = React.useState<string[]>([]);
  const [triggerPermissions, setTriggerPermissions] = React.useState(false);
  React.useEffect(() => {
    fetchMyPermissions().then((permissions) => setMyPermissions(permissions));
  }, [triggerPermissions]);
  const filteredPermissions = React.useMemo(() => {
    return permissionsList.filter((permission) => {
      return permission.description
        .toLowerCase()
        .includes(search.toLowerCase());
    });
  }, [search]);
  function handleSwitchChange(permission: string) {
    if (myPermissions.includes(permission)) {
      removePermission(permission).then(() => {
        setTriggerPermissions((trigger)=>!trigger);

      });
    } else {
      addPermission(permission).then(() => {
        setMyPermissions((permissions) => [...permissions, permission]);
        setTriggerPermissions((trigger)=>!trigger);
      });
    }
  }

  return (
    <div>
      <h1>Permissions</h1>
      <div>
        <Input
          type="text"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Tìm "
        />
      </div>
      <Table>
        <thead>
          <tr>
            <th>Value</th>
            <th>Description</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {filteredPermissions.map((permission, index) => {
            return (
              <tr key={index}>
                <td>{permission.name}</td>
                <td>{permission.description}</td>
                <td>
                  <FormGroup switch>
                    <Input type="switch" checked={myPermissions.includes(permission.name)} onChange={() => handleSwitchChange(permission.name)} />
                  </FormGroup>
                </td>
              </tr>
            );
          })}
        </tbody>
      </Table>
    </div>
  );
};

export default Permission;
