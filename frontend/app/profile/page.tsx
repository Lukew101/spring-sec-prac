"use client"
import { useSearchParams } from "next/navigation";
import Cookies from "js-cookie";
import { useQuery } from '@tanstack/react-query';
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import jwtDecode from "jwt-decode";


type UserDetails = {
  firstName: string,
  lastName: string,
  email: string,
  picture: string
}

export default function Home() {
  const [userDetails, setUserDetails] = useState<UserDetails>();

  // const [cookies, setCookie] = useCookies(["JwtToken"][0]);
  
  useEffect(() => {
    async function fetchData() {
      try {
        const response = await fetch("http://localhost:8080/user", {
          credentials: "include", // send what cookies you have back
        });
  
        if (!response.ok) {
          const text = await response.text();
          console.error(`HTTP Error! Status: ${response.status}, Response: ${text}`);
        }
        const data: UserDetails = await response.json();
        console.log(data);
        setUserDetails(data);
      } catch (error) {
        console.error("Error fetching data: " + error);
      }
    }
    fetchData();
//     console.log("Cookies:", cookies);
// console.log("Decoding token:", cookies.JwtToken);
// var decodedToken = jwtDecode(cookies.JwtToken);
// console.log("Decoded token:", decodedToken);
  }, []);

  const handleLogout = async () => {
    try {
        const response = await fetch("http://localhost:8080/logout", {
            method: "POST",
            credentials: "include", // send cookies with the request
        });
        if (response.ok) {
          console.log("here");
          
        } else {
            console.error("Logout failed");
        }
    } catch (error) {
        console.error("Error during logout: " + error);
    }
};

  if(userDetails) {
    return (
      <div>
      <h2>Personalized Page</h2>
      <a href='http://localhost:8080/login/oauth2/code/google'>Log in with Google</a>
      <button onClick={handleLogout}>Logout</button>    
      {userDetails ? (
        <div>
          <p>First Name: {userDetails.firstName}</p>
          <p>Last Name: {userDetails.lastName}</p>
          <p>Email: {userDetails.email}</p>
          <img src={userDetails.picture}></img>
        </div>
      ) : (
        <p>Loading user details...</p>
      )}
    </div>
    );
  }

  return (
    <a href='http://localhost:8080/login/oauth2/code/google'>Log in with Google</a>
  )

  
}


